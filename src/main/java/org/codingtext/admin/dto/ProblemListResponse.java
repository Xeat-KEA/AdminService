package org.codingtext.admin.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemListResponse {
    private int totalItems;
    private int totalPages;
    private List<ProblemDto> problems;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProblemDto {
        private Long id;
        private String title;
        private String difficulty;
        private String algorithm;
        private String permission;
    }
}
