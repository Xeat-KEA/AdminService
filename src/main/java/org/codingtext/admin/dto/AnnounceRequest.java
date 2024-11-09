package org.codingtext.admin.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnounceRequest {
    String title;
    String content;
    long adminId;
}
