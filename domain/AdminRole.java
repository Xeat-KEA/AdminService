package org.codingtext.admin.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminRole {
    GENERAL("일반 관리자 계정"), //생성된 모든 계정은 일반으로 처리
    ROOT("루트 관리자 계정"),
    NONE("아직 승인되지 않은 관리자 계정");


    private final String description;


    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static AdminRole fromDescription(String description) {
        for (AdminRole adminRole : AdminRole.values()) {
            if (adminRole.description.equalsIgnoreCase(description)) {
                return adminRole;
            }
        }
        throw new IllegalArgumentException("Unknown AdminRole description: " + description);
    }
}
