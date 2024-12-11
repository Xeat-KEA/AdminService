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
import org.codingtext.admin.dto.announce.AnnounceDetailResponse;
import org.codingtext.admin.dto.announce.AnnounceRequest;

import org.codingtext.admin.dto.announce.AnnounceResponse;
import org.codingtext.admin.dto.announce.AnnounceUpdateRequest;
import org.codingtext.admin.error.ErrorResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@Tag(name = "Admin", description = "관리자 설정 API")
public interface AdminApi {

    @Operation(summary = "관리자 승인 요청 처리",
            description = "승인 대기 중인 관리자를 승인하거나 거절합니다.",
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
    ResponseEntity<?> permitAdmin(String email, PermitRequest permitRequest);


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
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 처리 완료.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class, example = "관리자가 삭제되었습니다. 관리자 ID: 1"))),
                    @ApiResponse(responseCode = "403", description = "삭제 요청을 처리할 권한이 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 관리자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<String> deleteAdmin(String rootAdminEmail, Long adminId);


    @Operation(summary = "공지사항 생성",
            description = "관리자가 새로운 공지사항을 생성합니다.",
            requestBody = @RequestBody(
                    description = "공지사항 생성 요청 데이터",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnnounceRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 생성 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnnounceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 관리자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<?> createAnnounce(AnnounceRequest announceRequest);



    @Operation(summary = "공지사항 수정",
            description = "관리자가 공지사항을 수정합니다.",
            requestBody = @RequestBody(
                    description = "공지사항 수정 요청 데이터",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnnounceUpdateRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 수정 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnnounceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 관리자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<?> updateAnnounce(AnnounceUpdateRequest announceUpdateRequest);


    @Operation(
            summary = "공지사항 목록 조회",
            description = "공지사항 목록을 페이지네이션 형태로 조회합니다.(관리자의 겨",
            parameters = {
                    @Parameter(name = "page", description = "조회할 페이지 번호 (기본값: 0)", schema = @Schema(type = "integer")),
                    @Parameter(name = "size", description = "페이지 당 데이터 개수 (기본값: 10)", schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 목록 조회 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<?> getAnnouncements(int page, int size);

    @Operation(summary = "공지사항 상세 조회",
            description = "공지사항 ID를 기반으로 공지사항의 상세 정보를 조회합니다.",
            parameters = @Parameter(name = "announceId", description = "조회할 공지사항 ID", required = true, schema = @Schema(type = "long")),
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 상세 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnnounceDetailResponse.class))),
                    @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<?> getAnnouncementDetails(long announceId);


    @Operation(summary = "공지사항 삭제",
            description = "관리자가 공지사항 삭제한다.",
            parameters = @Parameter(name = "announceId", description = "삭제할 공지사항 ID", required = true, schema = @Schema(type = "long")),
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 식제 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnnounceDetailResponse.class))),
                    @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<?> deleteAnnounce(long announceId);
}

