package com.bupt.web.exception;


import com.bupt.web.common.enums.UimErrorEnum;
import com.bupt.web.common.enums.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常
 *
 * @author crescent
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebException extends RuntimeException {

    private String code;

    private String msg;


    public WebException() {
    }

    public WebException(ResultCodeEnum code){
        super(code.getDesc());
        this.code = code.getCode();
        this.msg = code.getDesc();
    }

    public WebException(UimErrorEnum code){
        super(code.getDesc());
        this.code = code.getCode();
        this.msg = code.getDesc();
    }

}
