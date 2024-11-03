package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;
import org.codingtext.admin.dto.*;
import org.codingtext.admin.error.exception.AdminNotFoundException;
import org.codingtext.admin.error.exception.InvalidPasswordException;
import org.codingtext.admin.error.exception.PermissionDeniedException;
import org.codingtext.admin.jwt.JwtProvider;
import org.codingtext.admin.jwt.JwtToken;
import org.codingtext.admin.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new InvalidPasswordException("Password does not match");
        }

        // 관리자 역할이 NONE인지 확인
        if (admin.getAdminRole() == AdminRole.NONE) {
            throw new PermissionDeniedException("Account is not approved for login");
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
}