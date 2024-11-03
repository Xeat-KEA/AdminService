package org.codingtext.admin.controller;
import lombok.RequiredArgsConstructor;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/none")
    public ResponseEntity<?> findNoneAccount() {
        return ResponseEntity.ok(adminService.findNoneAccount());
    }

    @PostMapping("/permit")
    public ResponseEntity<?> permitAdmin(
            @RequestHeader("AdminId") long adminId,
            @RequestBody PermitRequest permitRequest) {
        return ResponseEntity.ok(adminService.processAdminRequest(adminId, permitRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAllAdmins() {
        return ResponseEntity.ok(adminService.findAllAdmins());
    }
}