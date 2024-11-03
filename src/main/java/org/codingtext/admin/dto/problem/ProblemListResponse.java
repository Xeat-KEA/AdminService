package org.codingtext.admin.dto.problem;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemListResponse {
    private int totalItems;
    private int totalPages;
    private List<ProblemResponse> problems;
}
