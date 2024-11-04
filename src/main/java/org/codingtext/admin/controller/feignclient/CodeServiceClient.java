package org.codingtext.admin.controller.feignclient;

import org.codingtext.admin.dto.ProblemListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "code-bank-service", url = "http://172.16.211.69:8080")
public interface CodeServiceClient {
    @GetMapping("/code/lists")
    Page<ProblemListResponse> getProblemList(
            @RequestParam("page") int page,
            @RequestParam("size") int size);
}