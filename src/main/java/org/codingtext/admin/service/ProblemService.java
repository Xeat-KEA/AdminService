package org.codingtext.admin.service;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.controller.feignclient.CodeServiceClient;
import org.codingtext.admin.dto.problem.*;
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
        return codeServiceClient.createProblem(problemRequest);
    }
}
