package org.codingtext.admin.controller;
import lombok.RequiredArgsConstructor;
import org.codingtext.admin.dto.announce.AnnounceRequest;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.dto.announce.AnnounceResponse;
import org.codingtext.admin.dto.announce.AnnounceUpdateRequest;
import org.codingtext.admin.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController implements AdminApi{
    private final AdminService adminService;

    @PostMapping("/permit")
    public ResponseEntity<?> permitAdmin(
            @RequestHeader("Email") String email,
            @RequestBody PermitRequest permitRequest) {
        return ResponseEntity.ok(adminService.processAdminRequest(email, permitRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAllAdmins() {
        return ResponseEntity.ok(adminService.findAllAdmins());
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(
            @RequestHeader("Email") String rootAdminEmail,
            @PathVariable Long adminId) {
        String message = adminService.deleteAdmin(rootAdminEmail, adminId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/announce")
    public ResponseEntity<?> createAnnounce(
            @RequestHeader("Email") String email,
            @RequestBody AnnounceRequest announceRequest) {
        return ResponseEntity.ok(adminService.saveAnnounce(email, announceRequest));
    }

    @PutMapping("/announce")
    public ResponseEntity<?> updateAnnounce(
            @RequestHeader("Email") String email,
            @RequestBody AnnounceUpdateRequest announceUpdateRequest) {
        return ResponseEntity.ok(adminService.updateAnnounce(email, announceUpdateRequest));
    }

    @GetMapping("/announce")
    public ResponseEntity<Page<AnnounceResponse>> getAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));
        return ResponseEntity.ok(adminService.findAnnouncements(pageable));
    }

    @GetMapping("/announce/{announceId}")
    public ResponseEntity<?> getAnnouncementDetails(@PathVariable long announceId){
        return ResponseEntity.ok(adminService.findAnnounceDetails(announceId));
    }

    @DeleteMapping("/announce/{announceId}")
    public ResponseEntity<?> deleteAnnounce(@PathVariable long announceId) {
        return ResponseEntity.ok(adminService.deleteAnnounce(announceId));
    }
}