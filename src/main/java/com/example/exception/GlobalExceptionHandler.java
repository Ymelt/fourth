package com.example.exception;

import com.example.config.ErrorResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ErrorResult handle(ServiceException se){
        return ErrorResult.error(se.getCode(), se.getMessage());
    }
}
