package org.codingtext.admin.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.codingtext.admin.dto.auth.LoginRequest;

import org.codingtext.admin.dto.auth.LoginResponse;
import org.codingtext.admin.dto.auth.SignupRequest;

import org.codingtext.admin.service.AuthService;
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
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/email")
    public ResponseEntity<?> validEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(authService.checkEmailDuplicate(email));
    }
}