package org.codingtext.admin.dto.problem;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemRequest {
    private String title;
    private String difficulty;
    private String algorithm;
    private String content;
    private List<String> input;
    private String output;
}
