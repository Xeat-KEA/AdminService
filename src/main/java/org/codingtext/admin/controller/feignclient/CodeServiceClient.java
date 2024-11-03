package org.codingtext.admin.controller.feignclient;

import org.codingtext.admin.dto.ProblemListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "code-bank-service", url = "http://172.16.211.69:8080")
public interface CodeServiceClient {
    @GetMapping("/codes")
    List<ProblemListResponse> getProblemList();
}