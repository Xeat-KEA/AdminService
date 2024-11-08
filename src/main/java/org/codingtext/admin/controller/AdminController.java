package org.codingtext.admin.controller;
import lombok.RequiredArgsConstructor;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.dto.report.ReportReplyRequest;
import org.codingtext.admin.dto.report.ReportArticleRequest;
import org.codingtext.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
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

    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(
            @RequestHeader("AdminId") Long rootAdminId,
            @PathVariable Long adminId) {
        // 요청을 보낸 사용자의 권한 확인 후 삭제
        String message = adminService.deleteAdmin(rootAdminId, adminId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/report/articles")
    public ResponseEntity<?> saveReportArticle(@RequestBody ReportArticleRequest reportArticleRequest) {
        adminService.saveReportArticle(reportArticleRequest);
        return ResponseEntity.ok("Report Article saved successfully.");
    }

    @PostMapping("/report/replies")
    public ResponseEntity<?> saveReport(@RequestBody ReportReplyRequest reportReplyRequest) {
        adminService.saveReportReply(reportReplyRequest);
        return ResponseEntity.ok("Report Reply saved successfully.");
    }
}