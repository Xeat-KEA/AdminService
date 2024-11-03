package org.codingtext.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminRole {
    GENERAL("일반 관리자 계정"), //생성된 모든 계정은 일반으로 처리
    ROOT("루트 관리자 계정"),
    NONE("아직 승인되지 않은 관리자 계정");


    private final String description;


}
