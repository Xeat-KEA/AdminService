package org.codingtext.admin.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.codingtext.admin.dto.auth.LoginRequest;

import org.codingtext.admin.dto.auth.LoginResponse;
import org.codingtext.admin.dto.auth.SignupRequest;
import org.codingtext.admin.error.exception.RefreshTokenDoesNotMatchException;
import org.codingtext.admin.jwt.JwtToken;
import org.codingtext.admin.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.signup(signupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(loginRequest);
        response.addHeader("Authorization", loginResponse.getJwtToken().getAccessToken());
        response.addHeader("Refresh", loginResponse.getJwtToken().getRefreshToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/email")
    public ResponseEntity<?> validEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(authService.checkEmailDuplicate(email));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(
            @RequestHeader(name = "Refresh") String refreshToken,
            HttpServletResponse response) {

        Claims claims = authService.validateAndExtractClaims(refreshToken, "refresh");
        String email = claims.getSubject();

        String storedRefreshToken = authService.getRefreshToken(email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenDoesNotMatchException(HttpStatus.NOT_FOUND, "Refresh Token 이 일치하지 않습니다.");
        }
        String bearerAccessToken = authService.createBearerToken(email,"access", 1000 * 60 * 10L);
        String bearerRefreshToken = authService.createBearerToken(email,"refresh",1000 * 60 * 60 * 24L); // 24시간

        authService.saveRefreshToken(email, bearerRefreshToken);

        response.addHeader("Authorization", bearerAccessToken);
        response.addHeader("Refresh", bearerRefreshToken);
        return ResponseEntity.ok(new JwtToken(response.getHeader("Authorization"), response.getHeader("Refresh")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(name = "Email") String email,
            HttpServletResponse response) {

        authService.deleteRefreshToken(email);
        response.addHeader("Authorization", null);
        response.addHeader("Refresh", null);
        return ResponseEntity.ok(new JwtToken(null,null));
    }
}