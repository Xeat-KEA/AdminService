package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;
import org.codingtext.admin.dto.LoginRequest;
import org.codingtext.admin.dto.LoginResponse;
import org.codingtext.admin.dto.SignupRequest;
import org.codingtext.admin.dto.AdminResponse;
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
                .adminRole(AdminRole.GENERAL) //회원가입시 무조건 일반 관리자 계정
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        return AdminResponse.builder()
                .id(savedAdmin.getId())
                .email(savedAdmin.getEmail())
                .adminRole(savedAdmin.getAdminRole())
                .build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Admin admin = adminRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtProvider.createToken(admin.getEmail());
        AdminResponse adminResponse = AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .adminRole(admin.getAdminRole())
                .build();

        return new LoginResponse(new JwtToken(accessToken, null), adminResponse);
    }
}