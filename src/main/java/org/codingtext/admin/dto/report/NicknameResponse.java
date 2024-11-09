package org.codingtext.admin.dto.report;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameResponse {
    private Long id;
    private String nickname;
}