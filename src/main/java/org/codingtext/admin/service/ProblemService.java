package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.controller.feignclient.CodeServiceClient;
import org.codingtext.admin.dto.problem.*;
import org.codingtext.admin.error.exception.ProblemDeletionFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.codingtext.admin.error.exception.ProblemCreationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final CodeServiceClient codeServiceClient;

    public Page<ProblemListResponse> findAllCodeProblems(int page, int size) {
        return codeServiceClient.getProblemList(page, size);
    }

    @Transactional
    public ProblemResponse createProblem(ProblemRequest problemRequest) {
        ResponseEntity<ProblemResponse> response = codeServiceClient.createProblem(problemRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new ProblemCreationFailedException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
            throw new ProblemCreationFailedException("문제가 이미 존재합니다.", HttpStatus.CONFLICT);
        } else {
            throw new ProblemCreationFailedException("문제 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public String deleteProblem(Long codeId) {
        ResponseEntity<?> response = codeServiceClient.deleteProblem(codeId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return "삭제되었습니다.";
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ProblemDeletionFailedException("문제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new ProblemDeletionFailedException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        } else {
            throw new ProblemDeletionFailedException("문제 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
