package com.bupt.main.exception;


import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.common.response.ResponseData;
import com.bupt.kg.exception.KgException;
import com.bupt.web.exception.WebException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 控制层异常
 *
 * @author crescent73
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(KgException.class)
    ResponseData handelKgBusinessException(HttpServletRequest request, KgException e) {
        return ResponseData.setResult(e.getCode(),e.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(WebException.class)
    ResponseData handelWebBusinessException(HttpServletRequest request, WebException e) {
        return ResponseData.setResult(e.getCode(),e.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        if (ex.getBindingResult().hasErrors()) {
            List<FieldError> errors = ex.getBindingResult().getFieldErrors();
            return ResponseData.setResult(errors.get(0).getField() + "_error", errors.get(0).getDefaultMessage());
        }
        return ResponseData.setResult(ResultCodeEnum.ARGUMENT_NOT_VALID.getCode(),ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public ResponseData handleBindExceptionException(BindException ex) {
        if (ex.getBindingResult().hasErrors()) {
            List<FieldError> errors = ex.getBindingResult().getFieldErrors();
            return ResponseData.setResult(errors.get(0).getField() + "_error", errors.get(0).getDefaultMessage());
        }
        return ResponseData.setResult(ResultCodeEnum.ARGUMENT_NOT_VALID.getCode(),ex.getMessage());
    }
    /**
     * 方法调用错误使用
     *
     * @param request
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseData handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response,
                                                                HttpRequestMethodNotSupportedException ex) {
        response.setStatus(404);    // 设置为找不到页面
        log.info("Uri: " + request.getRequestURI() + ", Method: " + request.getMethod() + ", " + ex.getMessage());
        return ResponseData.setResult(ResultCodeEnum.INVALID_API);
    }

    /**
     * 如果 Jackson 解析参数错误，抛出 400 的错误.
     *
     * @param request 客户端请求.
     * @param ex      Jackson 异常
     * @return 400 的错误
     */
    @ResponseBody
    @ExceptionHandler({JsonProcessingException.class, HttpMessageNotReadableException.class})
    public ResponseData handleClientParamException(HttpServletRequest request, Exception ex) {
        log.debug("Request uri[{}] param error, ", request.getRequestURI(), ex);
        return ResponseData.setResult(ResultCodeEnum.ARGUMENT_NOT_VALID.getCode(),ex.getMessage());
    }

    /**
     * 总的异常：
     * <p>
     * Exception只有在找不到异常类型绑定时才会到达这里处理,
     * 这些异常无须让调用者知道，属于系统内部错误
     * </p>
     *
     * @param request
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData handleException(HttpServletRequest request, Exception ex) {
        log.error(request.getRequestURI(), ex);
        return ResponseData.setResult(ResultCodeEnum.SERVER_ERROR);
    }

}
