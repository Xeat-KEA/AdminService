package org.codingtext.admin.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {
    private String email;
    private String password;
}
