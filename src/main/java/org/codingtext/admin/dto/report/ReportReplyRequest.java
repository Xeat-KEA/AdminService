package org.codingtext.admin.dto.report;

import lombok.*;
import org.codingtext.admin.domain.ReportType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportReplyRequest {
    private long reporterId;
    private long blogId;
    private long articleId;
    private long replyId;
    private ReportType reportType;
    private String customDescription;
}
