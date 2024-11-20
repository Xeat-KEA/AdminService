package org.codingtext.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.codingtext.admin.domain.AdminRole;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminResponse {
    @Schema(description = "관리자 ID", example = "1")
    private Long id;

    @Schema(description = "관리자 이메일 주소", example = "admin@example.com")
    private String email;

    @Schema(description = "관리자 권한", example = "NONE")
    private AdminRole adminRole;
}
