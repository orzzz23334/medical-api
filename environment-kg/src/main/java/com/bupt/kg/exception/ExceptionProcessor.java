package com.bupt.kg.exception;

import com.bupt.kg.common.response.ResponseData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionProcessor {
    @ExceptionHandler(KgException.class)
    public ResponseData exceptionHandler(KgException e){
        // 日志记录
        return ResponseData.setResult(e.getCode(), e.getMsg());
    }

}
