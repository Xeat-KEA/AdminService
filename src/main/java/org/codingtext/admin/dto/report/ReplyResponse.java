package org.codingtext.admin.dto.report;

import lombok.*;
import org.codingtext.admin.domain.ReportType;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyResponse {
    Long articleId; //해당 게시글 표시'
    Long replyId;
    String replyTitle; //신고 댓글 제목
    String name; //신고자
    ReportType reportType; //신고사유
    LocalDate reportDate; //신고일
}
