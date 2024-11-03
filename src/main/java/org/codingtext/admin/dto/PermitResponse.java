package org.codingtext.admin.dto;
import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermitResponse {
    private Long adminId;
    private String message;
}