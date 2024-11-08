package org.codingtext.admin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "USER_REPORT")
public class UserReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Long id;

    @Column(name = "REPORTER_ID", nullable = false)
    private Long reporterId; //피신고자

    @Column(name = "BLOG_ID", nullable = false)
    private Long blogId;

    @Column(name = "ARTICLE_ID")
    private Long articleId;

    @Column(name = "REPLY_ID")
    private Long replyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "REPORT_TYPE", nullable = false)
    private ReportType reportType;

    @Column(name = "REPORT_CUSTOM_DESCRIPTION")
    private String customDescription; //신고사유 직접입력, ReportType이 Custom이 아닐 경우에는 null
}
