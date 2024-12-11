package org.codingtext.admin.dto.announce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnounceRequest {

    @Schema(description = "공지사항 제목", example = "2024년 첫 번째 공지")
    private String title;

    @Schema(description = "공지사항 내용", example = "2024년 첫 번째 공지사항 내용입니다.")
    private String content;
}
