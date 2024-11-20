package org.codingtext.admin.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {
    @Schema(description = "관리자의 이메일 주소", example = "admin@example.com")
    private String email;

    @Schema(description = "관리자의 비밀번호", example = "password123")
    private String password;
}
