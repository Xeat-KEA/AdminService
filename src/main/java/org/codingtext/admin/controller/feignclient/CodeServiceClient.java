package org.codingtext.admin.controller.feignclient;

import org.codingtext.admin.dto.problem.ProblemListResponse;
import org.codingtext.admin.dto.problem.ProblemRequest;
import org.codingtext.admin.dto.problem.ProblemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "code-bank-service", url = "http://172.16.211.69:8080")
public interface CodeServiceClient {
    @GetMapping("/codes")
    List<ProblemListResponse> getProblemList();

    @PostMapping("/codes")
    ResponseEntity<ProblemResponse> createProblem(@RequestBody ProblemRequest problemRequest);
}