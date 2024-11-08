package org.codingtext.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {
    INAPPROPRIATE_CONTENT("부적절한 내용"),
    SPAM("스팸 신고"),
    ABUSE("욕설 또는 비방"),
    COPYRIGHT("저작권 침해"),
    CUSTOM("직접 입력"),
    OTHER("기타");

    private final String description;
}

