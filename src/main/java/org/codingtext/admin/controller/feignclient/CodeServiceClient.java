package org.codingtext.admin.controller.feignclient;

import org.codingtext.admin.dto.problem.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "code-bank-service", url = "http://172.16.211.69:8080")
public interface CodeServiceClient {
    @GetMapping("/code")
    List<ProblemListResponse> getProblemList();

    @PostMapping("/code")
    ResponseEntity<ProblemResponse> createProblem(@RequestBody ProblemRequest problemRequest);

    @DeleteMapping("/code/{codeId}")
    ResponseEntity<?> deleteProblem(@PathVariable("codeId") long codeId);
}