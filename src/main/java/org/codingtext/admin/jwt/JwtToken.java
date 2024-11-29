package org.codingtext.admin.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
public class JwtToken {

    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1...")
    private final String accessToken;
}