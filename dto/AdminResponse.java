package org.codingtext.admin.dto;

import lombok.*;
import org.codingtext.admin.domain.AdminRole;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminResponse {
    private Long id;
    private String email;
    private AdminRole adminRole;
}
