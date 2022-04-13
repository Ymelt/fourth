package com.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult {
    private String code;
    private String msg;
    private Object data;

    public static ErrorResult error(String code,String msg){
        return new ErrorResult(code,msg,null);
    }
}
