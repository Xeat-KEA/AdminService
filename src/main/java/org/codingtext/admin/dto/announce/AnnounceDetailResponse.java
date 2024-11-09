package org.codingtext.admin.dto.announce;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnounceDetailResponse {
    private long announceId;
    private String title;
    private String content;
    private LocalDate createdDate;
}