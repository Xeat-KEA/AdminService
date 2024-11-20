package org.codingtext.admin.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermitResponse {
    @Schema(description = "승인/거절한 관리자 ID", example = "1")
    private Long adminId;

    @Schema(description = "응답 메시지", example = "관리자 승인 요청이 처리되었습니다. / 해당 계정이 삭제되었습니다.")
    private String message;
}