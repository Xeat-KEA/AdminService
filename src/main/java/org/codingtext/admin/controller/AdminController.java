package org.codingtext.admin.controller;
import lombok.RequiredArgsConstructor;
import org.codingtext.admin.dto.announce.AnnounceRequest;
import org.codingtext.admin.dto.announce.AnnounceResponse;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.dto.report.ArticleResponse;
import org.codingtext.admin.dto.report.ReplyRequest;
import org.codingtext.admin.dto.report.ArticleRequest;
import org.codingtext.admin.dto.report.ReplyResponse;
import org.codingtext.admin.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController implements AdminApi{
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
        return ResponseEntity.ok("게시글이 신고 처리 되었습니다.");
    }

    @PostMapping("/report/replies")
    public ResponseEntity<?> saveReportReply(@RequestBody ReplyRequest reportReplyRequest) {
        adminService.saveReportReply(reportReplyRequest);
        return ResponseEntity.ok("댓글이 신고 처리 되었습니다.");
    }

    @GetMapping("/report/articles")
    public ResponseEntity<Page<ArticleResponse>> getReportArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.findReportArticles(pageable));
    }

    @GetMapping("/report/replies")
    public ResponseEntity<Page<ReplyResponse>> getReportReplies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.findReportReplies(pageable));
    }

//
//    @PostMapping("/announce")
//    public ResponseEntity<?> createAnnounce(@RequestBody AnnounceRequest announceRequest) {
//        adminService.saveAnnounce(announceRequest);
//        return ResponseEntity.ok("Announcement created successfully.");
//    }
//
//    @GetMapping("/announce")
//    public ResponseEntity<Page<AnnounceResponse>> getAnnouncements(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok(adminService.findAnnouncements(pageable));
//    }
//
//    @GetMapping("/announce/{announceId}")
//    public ResponseEntity<?> getAnnouncements(@PathVariable long announceId){
//        return ResponseEntity.ok(adminService.findAnnounceDetails(announceId));
//    }
}