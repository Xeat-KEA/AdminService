package org.codingtext.admin.controller;
import lombok.RequiredArgsConstructor;
import org.codingtext.admin.dto.AnnounceRequest;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.dto.report.ArticleResponse;
import org.codingtext.admin.dto.report.ReplyRequest;
import org.codingtext.admin.dto.report.ArticleRequest;
import org.codingtext.admin.dto.report.ReplyResponse;
import org.codingtext.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        String message = adminService.deleteAdmin(rootAdminId, adminId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/report/articles")
    public ResponseEntity<?> saveReportArticle(@RequestBody ArticleRequest reportArticleRequest) {
        adminService.saveReportArticle(reportArticleRequest);
        return ResponseEntity.ok("Report Article saved successfully.");
    }

    @PostMapping("/report/replies")
    public ResponseEntity<?> saveReport(@RequestBody ReplyRequest reportReplyRequest) {
        adminService.saveReportReply(reportReplyRequest);
        return ResponseEntity.ok("Report Reply saved successfully.");
    }

    @GetMapping("/report/articles")
    public ResponseEntity<?> getReportArticles() {
        List<ArticleResponse> reports = adminService.findReportArticles();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/report/replies")
    public ResponseEntity<?> getReportReplies() {
        List<ReplyResponse> reports = adminService.findReportReplies();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/announce")
    public ResponseEntity<?> createAnnounce(@RequestBody AnnounceRequest announceRequest) {
        adminService.saveAnnounce(announceRequest);
        return ResponseEntity.ok("Announcement created successfully.");
    }
}