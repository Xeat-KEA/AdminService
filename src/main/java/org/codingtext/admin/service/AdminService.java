package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.controller.feignclient.BlogServiceClient;
import org.codingtext.admin.controller.feignclient.UserServiceClient;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;
import org.codingtext.admin.domain.Announce;
import org.codingtext.admin.domain.UserReport;
import org.codingtext.admin.dto.*;
import org.codingtext.admin.dto.announce.AnnounceDetailResponse;
import org.codingtext.admin.dto.announce.AnnounceRequest;
import org.codingtext.admin.dto.announce.AnnounceResponse;
import org.codingtext.admin.dto.report.*;
import org.codingtext.admin.error.exception.AdminNotFoundException;
import org.codingtext.admin.error.exception.AnnounceNotFoundException;
import org.codingtext.admin.error.exception.PermissionDeniedException;
import org.codingtext.admin.repository.AdminRepository;
import org.codingtext.admin.repository.AnnounceRepository;
import org.codingtext.admin.repository.UserReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserReportRepository userReportRepository;
    private final BlogServiceClient blogServiceClient;
    private final UserServiceClient userServiceClient;
    private final AnnounceRepository announceRepository;

    public List<AdminResponse> findNoneAccount() {
        // DB에서 NONE 역할의 Admin을 조회하고 DTO로 변환
        return adminRepository.findByAdminRole(AdminRole.NONE).stream()
                .map(admin -> AdminResponse.builder()
                        .id(admin.getId())
                        .email(admin.getEmail())
                        .adminRole(admin.getAdminRole()) // Enum을 문자열로 변환
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PermitResponse processAdminRequest(long adminId, PermitRequest permitRequest) {
        // root 조회
        Admin rootAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Root admin not found"));
        // none 조회
        Admin noneAdmin = adminRepository.findById(permitRequest.getAdminId())
                .orElseThrow(() -> new AdminNotFoundException("None admin not found"));
        // ROLE이 ROOT인 경우에만 처리
        if (rootAdmin.getAdminRole() == AdminRole.ROOT) {
            if (permitRequest.getIsPermit()) {
                // NONE 관리자 권한을 GENERAL로 변경
                adminRepository.save(noneAdmin.toBuilder()
                        .adminRole(AdminRole.GENERAL)
                        .build());
                return PermitResponse.builder()
                        .adminId(permitRequest.getAdminId())
                        .message("권한이 GENERAL로 변경되었습니다.")
                        .build();
            } else {
                adminRepository.deleteById(permitRequest.getAdminId());
                return PermitResponse.builder()
                        .adminId(permitRequest.getAdminId())
                        .message("해당 계정이 삭제되었습니다.")
                        .build();
            }
        } else {
            throw new PermissionDeniedException("Only ROOT accounts can process this request.");
        }
    }

    public List<AdminResponse> findAllAdmins() {
        List<Admin> admins = adminRepository.findAll(); // 모든 관리자 조회
        return admins.stream()
                .map(admin -> AdminResponse.builder()
                        .id(admin.getId())
                        .email(admin.getEmail())
                        .adminRole(admin.getAdminRole())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public String deleteAdmin(Long rootAdminId, Long adminId) {
        Admin rootAdmin = adminRepository.findById(rootAdminId)
                .orElseThrow(() -> new AdminNotFoundException("root admin not found"));
        Admin generalAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin to delete not found"));
        // 요청자가 ROOT 권한을 가지고 있는 경우에만 삭제
        if (rootAdmin.getAdminRole() == AdminRole.ROOT) {
            adminRepository.delete(generalAdmin);
            return "Admin with ID " + adminId + " has been deleted.";
        } else {
            throw new PermissionDeniedException("Only ROOT accounts can delete admins.");
        }
    }


    @Transactional
    public void saveReportArticle(ArticleRequest reportArticleRequest) {
        userReportRepository.save(UserReport.builder()
                .reportingUserId(reportArticleRequest.getReporterId())
                .reportedUserId(reportArticleRequest.getReportedId())
                .articleId(reportArticleRequest.getArticleId())
                .reportType(reportArticleRequest.getReportType())
                .customDescription(reportArticleRequest.getCustomDescription())
                .build());
    }

    @Transactional
    public void saveReportReply(ReplyRequest reportReplyRequest) {
        userReportRepository.save(UserReport.builder()
                .reportingUserId(reportReplyRequest.getReporterId())
                .reportedUserId(reportReplyRequest.getReportedId())
                .articleId(reportReplyRequest.getArticleId())
                .replyId(reportReplyRequest.getReplyId())
                .reportType(reportReplyRequest.getReportType())
                .customDescription(reportReplyRequest.getCustomDescription())
                .build());
    }

    // 게시글 신고 내역 조회
    public Page<ArticleResponse> findReportArticles(Pageable pageable) {
        // 페이징된 UserReport 조회
        Page<UserReport> reportsPage = userReportRepository.findAll(pageable);
        List<UserReport> reports = reportsPage.getContent();

        // articleId와 userId 리스트 추출
        List<Long> articleIds = reports.stream()
                .map(UserReport::getArticleId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> userIds = reports.stream()
                .map(UserReport::getReportingUserId)
                .distinct()
                .collect(Collectors.toList());

        // 외부 서비스에서 제목 및 닉네임 데이터 가져오기
        Map<Long, String> articleIdToTitleMap = blogServiceClient.getTitlesByIds(articleIds).stream()
                .collect(Collectors.toMap(TitleResponse::getId, TitleResponse::getTitle));
        Map<Long, String> userIdToNicknameMap = userServiceClient.getNicknamesByIds(userIds).stream()
                .collect(Collectors.toMap(NicknameResponse::getId, NicknameResponse::getNickname));

        // ArticleResponse 리스트 생성
        List<ArticleResponse> articleResponses = reports.stream()
                .map(userReport -> ArticleResponse.builder()
                        .articleId(userReport.getArticleId())
                        .articleTitle(articleIdToTitleMap.getOrDefault(userReport.getArticleId(), "Unknown Title"))
                        .name(userIdToNicknameMap.getOrDefault(userReport.getReportingUserId(), "Unknown Nickname"))
                        .reportType(userReport.getReportType())
                        .reportDate(userReport.getCreatedAt().toLocalDate())
                        .build())
                .collect(Collectors.toList());

        // PageImpl을 사용해 Page<ArticleResponse> 생성 및 반환
        return new PageImpl<>(articleResponses, pageable, reportsPage.getTotalElements());
    }

    public Page<ReplyResponse> findReportReplies(Pageable pageable) {
        Page<UserReport> reportsPage = userReportRepository.findAll(pageable);
        List<UserReport> reports = reportsPage.getContent(); // 현재 페이지의 데이터 목록

        List<Long> replyIds = reports.stream()
                .map(UserReport::getReplyId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> userIds = reports.stream()
                .map(UserReport::getReportingUserId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> replyIdToTitleMap = blogServiceClient.getRepliesByIds(replyIds).stream()
                .collect(Collectors.toMap(TitleResponse::getId, TitleResponse::getTitle));
        Map<Long, String> userIdToNicknameMap = userServiceClient.getNicknamesByIds(userIds).stream()
                .collect(Collectors.toMap(NicknameResponse::getId, NicknameResponse::getNickname));

        List<ReplyResponse> replyResponses = reports.stream()
                .map(userReport -> ReplyResponse.builder()
                        .replyId(userReport.getReplyId())
                        .replyTitle(replyIdToTitleMap.getOrDefault(userReport.getReplyId(), "Unknown Title"))
                        .name(userIdToNicknameMap.getOrDefault(userReport.getReportingUserId(), "Unknown Nickname"))
                        .reportType(userReport.getReportType())
                        .reportDate(userReport.getCreatedAt().toLocalDate())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(replyResponses, pageable, reportsPage.getTotalElements());
    }


    @Transactional
    public void saveAnnounce(AnnounceRequest announceRequest) {
        // Admin 존재 여부 확인
        Admin admin = adminRepository.findById(announceRequest.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Admin ID"));

        // Announce 생성
        announceRepository.save(Announce.builder()
                .title(announceRequest.getTitle())
                .content(announceRequest.getContent())
                .admin(admin)
                .build());
    }

    public Page<AnnounceResponse> findAnnouncements(Pageable pageable) {
        Page<Announce> announcePage = announceRepository.findAll(pageable);

        List<AnnounceResponse> announceResponses = announcePage.getContent().stream()
                .map(announce -> new AnnounceResponse(
                        announce.getId(),
                        announce.getTitle(),
                        announce.getCreatedAt().toLocalDate()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(announceResponses, pageable, announcePage.getTotalElements());
    }

    public AnnounceDetailResponse findAnnounceDetails(long announceId) {
        Announce announce = announceRepository.findById(announceId)
                .orElseThrow(() -> new AnnounceNotFoundException("announcement not found"));

        return AnnounceDetailResponse.builder()
                .announceId(announce.getId())
                .title(announce.getTitle())
                .content(announce.getContent())
                .createdDate(announce.getCreatedAt().toLocalDate())
                .build();
    }
}