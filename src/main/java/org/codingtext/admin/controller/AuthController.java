package org.codingtext.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codingtext.admin.dto.LoginRequest;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.dto.SignupRequest;
import org.codingtext.admin.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.signup(signupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/none")
    public ResponseEntity<?> findNoneAccount() {
        return ResponseEntity.ok(authService.findNoneAccount());
    }

    @PostMapping("/permit")
    public ResponseEntity<?> permitAdmin(
            @RequestHeader("AdminId") long adminId,
            @RequestBody PermitRequest permitRequest) {
        return ResponseEntity.ok(authService.processAdminRequest(adminId, permitRequest));
    }
}