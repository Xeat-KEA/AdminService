package org.codingtext.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codingtext.admin.dto.AdminResponse;
import org.codingtext.admin.dto.PermitRequest;
import org.codingtext.admin.dto.PermitResponse;
import org.codingtext.admin.dto.report.ArticleRequest;
import org.codingtext.admin.dto.report.ReplyRequest;
import org.codingtext.admin.error.ErrorResponse;
import org.springframework.http.ResponseEntity;


@Tag(name = "Admin", description = "관리자 설정 API")
public interface AdminApi {

    @Operation(
            summary = "승인 대기 관리자 조회",
            description = "승인되지 않은 관리자 계정을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "승인 대기 중인 관리자 목록 조회 성공",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdminResponse.class)))),
                    @ApiResponse(responseCode = "404", description = "승인 대기 중인 관리자를 찾을 수 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<?> findNoneAccount();


    @Operation(summary = "관리자 승인 요청 처리",
            description = "승인 대기 중인 관리자를 승인하거나 거절합니다.",
            parameters = {
                    @Parameter(name = "AdminId", description = "승인 요청을 처리하는 루트 관리자 ID", required = true, in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 승인/거절 처리 완료.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermitResponse.class))),
                    @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못되었습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "승인 요청을 처리할 권한이 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 관리자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> permitAdmin(long adminId, PermitRequest permitRequest);


    @Operation(summary = "관리자 전체 목록 조회",
            description = "등록된 모든 관리자 계정을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 관리자 목록 조회 완료.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdminResponse.class)))),
                    @ApiResponse(responseCode = "404", description = "데이터 조회 실패. 관리자 목록을 조회할 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            })
    ResponseEntity<?> findAllAdmins();


    @Operation(summary = "관리자 계정 삭제",
            description = "특정 관리자 계정을 삭제합니다.",
            parameters = {
                    @Parameter(name = "AdminId", description = "삭제 요청을 처리하는 루트 관리자 ID", required = true, in = ParameterIn.HEADER),
                    @Parameter(name = "adminId", description = "삭제할 관리자 ID", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 처리 완료.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class, example = "관리자가 삭제되었습니다. 관리자 ID: 1"))),
                    @ApiResponse(responseCode = "403", description = "삭제 요청을 처리할 권한이 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 관리자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<String> deleteAdmin(Long rootAdminId, Long adminId);


    @Operation(
            summary = "신고된 게시글 저장",
            description = "신고된 게시글 정보를 저장합니다.",
            requestBody = @RequestBody(description = "신고된 게시글 정보",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글이 신고 처리 되었습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못되었습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    ResponseEntity<?> saveReportArticle(ArticleRequest reportArticleRequest);


    @Operation(summary = "신고된 댓글 저장",
            description = "신고된 댓글 정보를 저장합니다.",
            requestBody = @RequestBody(description = "신고된 댓글 정보",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReplyRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글이 신고 처리 되었습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못되었습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    ResponseEntity<?>saveReportReply(ReplyRequest reportReplyRequest);


    @Operation(summary = "신고된 게시글 조회",
            description = "신고된 게시글 목록을 페이지네이션하여 조회합니다.",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (기본값: 0)"),
                    @Parameter(name = "size", description = "페이지 크기 (기본값: 10)")
            })
    ResponseEntity<?> getReportArticles(int page, int size);


    @Operation(summary = "신고된 댓글 조회",
            description = "신고된 댓글 목록을 페이지네이션하여 조회합니다.",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (기본값: 0)"),
                    @Parameter(name = "size", description = "페이지 크기 (기본값: 10)")
            })
    ResponseEntity<?> getReportReplies(int page, int size);
}

