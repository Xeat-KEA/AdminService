package org.codingtext.admin.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.codingtext.admin.dto.AdminResponse;
import org.codingtext.admin.jwt.JwtToken;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    @Schema(description = "JWT 토큰 정보")
    private JwtToken jwtToken;

    @Schema(description = "관리자 정보")
    private AdminResponse adminResponse;
}
