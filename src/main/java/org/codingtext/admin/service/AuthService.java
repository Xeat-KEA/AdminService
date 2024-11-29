package org.codingtext.admin.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;
import org.codingtext.admin.domain.RefreshToken;
import org.codingtext.admin.dto.*;
import org.codingtext.admin.dto.auth.LoginRequest;
import org.codingtext.admin.dto.auth.LoginResponse;
import org.codingtext.admin.dto.auth.SignupRequest;
import org.codingtext.admin.error.exception.*;
import org.codingtext.admin.jwt.JwtProvider;
import org.codingtext.admin.jwt.JwtToken;
import org.codingtext.admin.repository.AdminRepository;
import org.codingtext.admin.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
                .orElseThrow(() -> new AdminNotFoundException("요청한 관리자를 찾을 수 없습니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        // 관리자 역할이 NONE인지 확인
        if (admin.getAdminRole() == AdminRole.NONE) {
            throw new PermissionDeniedException("관리자 승인이 되지 않았습니다.");
        }

        // JWT 토큰 생성
        String accessToken = createBearerToken(admin.getEmail(), "access", 1000 * 60 * 10L);
        String refreshToken = createBearerToken(admin.getEmail(), "refresh", 1000 * 60 * 60 * 24L);

        // Admin 정보를 포함한 응답 객체 생성
        AdminResponse adminResponse = AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .adminRole(admin.getAdminRole())
                .build();

        return new LoginResponse(new JwtToken(accessToken), adminResponse);
    }

    public Claims validateAndExtractClaims(String token, String tokenType) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!jwtProvider.validateToken(token)) {
            throw new UnauthenticatedException(HttpStatus.UNAUTHORIZED, "토큰 유효하지 않습니다.");
        }
        Claims claims = jwtProvider.getUserInfoFromToken(token);
        String type = claims.get("type", String.class);

        if (!type.equals(tokenType)) {
            throw new TokenTypeMismatchException(HttpStatus.NOT_FOUND, "토큰 타입이 맞지 않음");
        }
        return claims;
    }

    public String getRefreshToken(String email) {
        RefreshToken token = refreshTokenRepository.findById(email).orElse(null);
        return token != null ? token.getRefreshToken() : null;
    }

    @Transactional
    public void saveRefreshToken(String email, String refreshToken) {
        RefreshToken token = new RefreshToken(email, refreshToken);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        refreshTokenRepository.deleteById(email);
    }

    public String createBearerToken(String email, String type,  Long expireMs) {
        return "Bearer " + jwtProvider.createToken(email, type,  expireMs);
    }

    public boolean checkEmailDuplicate(String email) {
        return adminRepository.existsByEmail(email);
    }
}