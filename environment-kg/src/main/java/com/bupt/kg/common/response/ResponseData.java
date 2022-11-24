package com.bupt.kg.common.response;

import com.bupt.kg.common.enums.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回数据：
 * code: 返回码
 * msg: 结果提示信息
 * data:结果数据信息
 */
@Data
@NoArgsConstructor
@ApiModel(description = "响应对象")
public class ResponseData<T> {
    @ApiModelProperty(value = "响应码", name = "code", required = true, example = "200")
    private String code; //结果返回码
    @ApiModelProperty(value = "响应消息", name = "msg", required = true, example = "OK")
    private String msg;  //结果提示信息
    @ApiModelProperty(value = "响应数据", name = "data")
    private T data; //结果数据信息

    public void setResultCodeEnum(ResultCodeEnum code) {
        this.code = code.getCode();
        this.msg = code.getDesc();
    }

    public static ResponseData success(){
        ResponseData responseData = new ResponseData();
        responseData.setResultCodeEnum(ResultCodeEnum.OK);
        return responseData;
    }

    public static<T> ResponseData<T> setResult(ResultCodeEnum code, T data){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setResultCodeEnum(code);
        responseData.setData(data);
        return responseData;
    }

    public static ResponseData setResult(ResultCodeEnum code){
        ResponseData responseData = new ResponseData();
        responseData.setResultCodeEnum(code);
        return responseData;
    }

    public static ResponseData setResult(String code,String msg){
        ResponseData responseData = new ResponseData();
        responseData.setCode(code);
        responseData.setMsg(msg);
        return responseData;
    }
}