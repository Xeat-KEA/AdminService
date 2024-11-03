package org.codingtext.admin.service;
import lombok.RequiredArgsConstructor;
import org.codingtext.admin.domain.Admin;
import org.codingtext.admin.domain.AdminRole;
import org.codingtext.admin.dto.AdminResponse;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.dto.PermitResponse;
import org.codingtext.admin.repository.AdminRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
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

    public PermitResponse processAdminRequest(long adminId, PermitRequest permitRequest) {
        // root 조회
        Admin rootAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Root admin not found"));
        // none 조회
        Admin noneAdmin = adminRepository.findById(permitRequest.getAdminId())
                .orElseThrow(() -> new RuntimeException("None admin not found"));
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
            throw new RuntimeException("Only ROOT accounts can process this request.");
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

    public String deleteAdmin(Long rootAdminId, Long adminId) {
        Admin rootAdmin = adminRepository.findById(rootAdminId)
                .orElseThrow(() -> new RuntimeException("root admin not found"));
        Admin generalAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin to delete not found"));
        // 요청자가 ROOT 권한을 가지고 있는 경우에만 삭제
        if (rootAdmin.getAdminRole() == AdminRole.ROOT) {
            adminRepository.delete(generalAdmin);
            return "Admin with ID " + adminId + " has been deleted.";
        } else {
            throw new RuntimeException("Only ROOT accounts can delete admins.");
        }
        //TODO: root가 자기자신을 삭제하는 경우에 대한 처리
    }
}