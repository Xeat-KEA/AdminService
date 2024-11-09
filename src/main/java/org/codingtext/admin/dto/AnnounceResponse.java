package org.codingtext.admin.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnounceResponse {
    private long announceId;
    private String title;
    private String content;
    private long adminId;
}