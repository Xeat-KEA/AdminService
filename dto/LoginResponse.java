package org.codingtext.admin.dto;

import lombok.*;
import org.codingtext.admin.jwt.JwtToken;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    private JwtToken jwtToken;
    private AdminResponse adminResponse;
}
