package org.codingtext.admin.dto.announce;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnounceRequest {
    private String title;
    private String content;
    private long adminId;
}
