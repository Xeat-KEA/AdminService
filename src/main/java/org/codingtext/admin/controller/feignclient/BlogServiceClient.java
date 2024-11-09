package org.codingtext.admin.controller.feignclient;


import org.codingtext.admin.dto.report.TitleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "blog-service", url = "http://localhost:8081") // 블로그 서비스 URL
public interface BlogServiceClient {

    @GetMapping("/articles/titles")
    List<TitleResponse> getTitlesByIds(@RequestBody List<Long> articleIds);
}