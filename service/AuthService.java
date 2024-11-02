package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.dto.LoginRequest;
import org.codingtext.admin.dto.SignupRequest;
import org.codingtext.admin.jwt.JwtProvider;
import org.codingtext.admin.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void signup(SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        Admin admin = Admin.builder()
                .email(signupRequest.getEmail())
                .password(encodedPassword)
                .build();

        adminRepository.save(admin);
    }

    public String login(LoginRequest loginRequest) {
        Admin admin = adminRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtProvider.createToken(admin.getEmail());
    }
}