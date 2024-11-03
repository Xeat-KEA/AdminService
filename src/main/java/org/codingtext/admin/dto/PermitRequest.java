package org.codingtext.admin.dto;
import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermitRequest {
    private Long adminId;
    private Boolean isPermit;
}