package org.codingtext.admin.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermitRequest {
    @Schema(description = "승인할 관리자 ID", example = "1")
    private Long adminId;

    @Schema(description = "승인 여부 (true: 승인, false: 거절)", example = "true")
    private Boolean isPermit;
}