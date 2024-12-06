package com.deal.carrot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private boolean result;
    private String msg;
    private Object data;

    public ResponseDTO(boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }
}
