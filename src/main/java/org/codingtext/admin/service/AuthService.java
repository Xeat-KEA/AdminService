package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;
import org.codingtext.admin.dto.*;
import org.codingtext.admin.jwt.JwtProvider;
import org.codingtext.admin.jwt.JwtToken;
import org.codingtext.admin.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AdminResponse signup(SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        Admin admin = Admin.builder()
                .email(signupRequest.getEmail())
                .password(encodedPassword)
                .adminRole(AdminRole.NONE) //회원가입시 무조건 일반 관리자 계정(승인되면 GENERAL로 바꿔주어야함)
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        return AdminResponse.builder()
                .id(savedAdmin.getId())
                .email(savedAdmin.getEmail())
                .adminRole(savedAdmin.getAdminRole())
                .build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // 이메일로 Admin 조회
        Admin admin = adminRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 관리자 역할이 NONE인지 확인
        if (admin.getAdminRole() == AdminRole.NONE) {
            throw new RuntimeException("Account is not approved for login");
        }

        // JWT 토큰 생성
        String accessToken = jwtProvider.createToken(admin.getId(),admin.getEmail());

        // Admin 정보를 포함한 응답 객체 생성
        AdminResponse adminResponse = AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .adminRole(admin.getAdminRole())
                .build();

        return new LoginResponse(new JwtToken(accessToken, null), adminResponse);
    }

    public List<AdminResponse> findNoneAccount() {
        // DB에서 NONE 역할의 Admin을 조회하고 DTO로 변환
        return adminRepository.findByAdminRole(AdminRole.NONE).stream()
                .map(admin -> AdminResponse.builder()
                        .id(admin.getId())
                        .email(admin.getEmail())
                        .adminRole(admin.getAdminRole()) // Enum을 문자열로 변환
                        .build())
                .collect(Collectors.toList());
    }

    public PermitResponse processAdminRequest(long adminId, PermitRequest permitRequest) {
        // root 조회
        Admin rootAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Root admin not found"));
        // none 조회
        Admin noneAdmin = adminRepository.findById(permitRequest.getAdminId())
                .orElseThrow(() -> new RuntimeException("None admin not found"));
        // ROLE이 ROOT인 경우에만 처리
        if (rootAdmin.getAdminRole() == AdminRole.ROOT) {
            if (permitRequest.getIsPermit()) {
                // NONE 관리자 권한을 GENERAL로 변경
                adminRepository.save(noneAdmin.toBuilder()
                        .adminRole(AdminRole.GENERAL)
                        .build());
                return PermitResponse.builder()
                        .adminId(permitRequest.getAdminId())
                        .message("권한이 GENERAL로 변경되었습니다.")
                        .build();
            } else {
                adminRepository.deleteById(permitRequest.getAdminId());
                return PermitResponse.builder()
                        .adminId(permitRequest.getAdminId())
                        .message("해당 계정이 삭제되었습니다.")
                        .build();
            }
        } else {
            throw new RuntimeException("Only ROOT accounts can process this request.");
        }
    }
}