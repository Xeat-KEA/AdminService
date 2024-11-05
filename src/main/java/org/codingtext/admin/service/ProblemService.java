package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.controller.feignclient.CodeServiceClient;
import org.codingtext.admin.dto.problem.*;
import org.codingtext.admin.error.exception.ProblemCreationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final CodeServiceClient codeServiceClient;

    public List<ProblemListResponse> findAllCodeProblems() {
        return codeServiceClient.getProblemList();
    }

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
}
