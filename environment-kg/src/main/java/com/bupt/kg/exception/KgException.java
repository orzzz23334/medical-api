package com.bupt.kg.exception;


import com.bupt.kg.common.enums.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常
 *
 * @author crescent
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KgException extends RuntimeException {

    private String code;

    private String msg;


    public KgException() {
    }

    public KgException(ResultCodeEnum code){
        super(code.getDesc());
        this.code = code.getCode();
        this.msg = code.getDesc();
    }

}
