package com.example.junitstudy.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CMResponseDto<T> {

    private Integer code; // 1 성공, -1 실패
    private String msg; // 에러, 성공 메시지
    private T body;

    @Builder
    public CMResponseDto(Integer code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }
}
