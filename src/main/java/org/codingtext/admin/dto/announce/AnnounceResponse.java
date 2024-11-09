package org.codingtext.admin.dto.announce;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnounceResponse {
    private long announceId;
    private String title;
    private LocalDate createdDate;
}