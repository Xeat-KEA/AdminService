package org.codingtext.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.codingtext.admin.dto.AdminResponse;
import org.codingtext.admin.dto.auth.LoginRequest;
import org.codingtext.admin.dto.auth.LoginResponse;
import org.codingtext.admin.dto.auth.SignupRequest;
import org.codingtext.admin.error.ErrorResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 관리 API")
public interface AuthApi {

    @Operation(summary = "회원가입",
            description = "이메일, 비밀번호를 입력하여 회원가입을 진행한다.",
            requestBody = @RequestBody(description = "회원가입 요청 데이터", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    ResponseEntity<?> signup(SignupRequest signupRequest);

    @Operation(summary = "로그인",
            description = "이메일과 비밀번호를 사용하여 로그인을 진행한다.",
            requestBody = @RequestBody(description = "로그인 요청 데이터", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인  성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> login(LoginRequest loginRequest, HttpServletResponse response);


    @Operation(summary = "이메일 중복 검증",
            description = "이메일 중복 검증을 진행한다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "중복 검사 완료 (true/false)")
                  })
    ResponseEntity<?> validEmail(String email);
}
