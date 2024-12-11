package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;

import org.codingtext.admin.domain.Announce;

import org.codingtext.admin.dto.*;
import org.codingtext.admin.dto.announce.*;

import org.codingtext.admin.error.exception.AdminNotFoundException;
import org.codingtext.admin.error.exception.AnnounceNotFoundException;
import org.codingtext.admin.error.exception.PermissionDeniedException;
import org.codingtext.admin.repository.AdminRepository;
import org.codingtext.admin.repository.AnnounceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final AdminRepository adminRepository;
    private final AnnounceRepository announceRepository;

    @Transactional
    public PermitResponse processAdminRequest(String email, PermitRequest permitRequest) {
        // root 조회
        Admin rootAdmin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("요청한 관리자를 찾을 수 없습니다."));
        // none 조회
        Admin noneAdmin = adminRepository.findById(permitRequest.getAdminId())
                .orElseThrow(() -> new AdminNotFoundException("요청한 관리자를 찾을 수 없습니다."));
        // ROLE이 ROOT인 경우에만 처리
        if (rootAdmin.getAdminRole() == AdminRole.ROOT) {
            if (permitRequest.getIsPermit()) {
                // NONE 관리자 권한을 GENERAL로 변경
                adminRepository.save(noneAdmin.toBuilder()
                        .adminRole(AdminRole.GENERAL)
                        .build());
                return PermitResponse.builder()
                        .adminId(permitRequest.getAdminId())
                        .message("관리자 승인 요청이 처리되었습니다.")
                        .build();
            } else {
                adminRepository.deleteById(permitRequest.getAdminId());
                return PermitResponse.builder()
                        .adminId(permitRequest.getAdminId())
                        .message("해당 계정이 삭제되었습니다.")
                        .build();
            }
        } else {
            throw new PermissionDeniedException("승인 요청을 처리할 권한이 없습니다.");
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
    public String deleteAdmin(String rootAdminEmail, Long adminId) {
        Admin rootAdmin = adminRepository.findByEmail(rootAdminEmail)
                .orElseThrow(() -> new AdminNotFoundException("루트 관리자를 찾을 수 없습니다."));
        Admin generalAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("요청한 관리자를 찾을 수 없습니다."));
        // 요청자가 ROOT 권한을 가지고 있는 경우에만 삭제
        if (rootAdmin.getAdminRole() == AdminRole.ROOT) {
            adminRepository.delete(generalAdmin);
            return "관리자가 삭제되었습니다. 관리자 ID: " + adminId;
        } else {
            throw new PermissionDeniedException("삭제 요청을 처리할 권한이 없습니다.");
        }
    }

    @Transactional
    public AnnounceDetailResponse saveAnnounce(String email, AnnounceRequest announceRequest) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("요청한 관리자를 찾을 수 없습니다."));

        // Base64 디코딩
        String decodedContent = new String(Base64.getDecoder().decode(announceRequest.getContent()), StandardCharsets.UTF_8);

        Announce announce = announceRepository.save(Announce.builder()
                .title(announceRequest.getTitle())
                .content(decodedContent)
                .admin(admin)
                .build());

        return AnnounceDetailResponse.builder()
                .announceId(announce.getId())
                .title(announce.getTitle())
                .content(announceRequest.getContent())
                .createdDate(announce.getCreatedAt().toLocalDate())
                .build();
    }

    @Transactional
    public AnnounceDetailResponse updateAnnounce(String email, AnnounceUpdateRequest announceUpdateRequest) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("요청한 관리자를 찾을 수 없습니다."));
        Announce announce = announceRepository.findById(announceUpdateRequest.getAnnounceId())
                .orElseThrow(() -> new AnnounceNotFoundException("공지사항을 찾을 수 없습니다."));

        // Base64 디코딩
        String decodedContent = new String(Base64.getDecoder().decode(announceUpdateRequest.getContent()), StandardCharsets.UTF_8);

        Announce updatedAnnounce = announceRepository.save(announce.toBuilder()
                .title(announceUpdateRequest.getTitle())
                .content(decodedContent)
                .admin(admin)
                .build());

        return AnnounceDetailResponse.builder()
                .announceId(updatedAnnounce.getId())
                .title(updatedAnnounce.getTitle())
                .content(announceUpdateRequest.getContent())
                .createdDate(announce.getCreatedAt().toLocalDate())
                .build();
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
                .orElseThrow(() -> new AnnounceNotFoundException("공지사항이 없습니다."));

        // Base64 인코딩
        String encodedContent = Base64.getEncoder().encodeToString(announce.getContent().getBytes());

        return AnnounceDetailResponse.builder()
                .announceId(announce.getId())
                .title(announce.getTitle())
                .content(encodedContent)
                .createdDate(announce.getCreatedAt().toLocalDate())
                .build();
    }

    @Transactional
    public AnnounceResponse deleteAnnounce(long announceId) {
        Announce announce = announceRepository.findById(announceId)
                .orElseThrow(() -> new AnnounceNotFoundException("공지사항을 찾을 수 없습니다."));

        announceRepository.delete(announce);

        return AnnounceResponse.builder()
                .announceId(announceId)
                .title(announce.getTitle())
                .createdDate(announce.getCreatedAt().toLocalDate())
                .build();
    }
}