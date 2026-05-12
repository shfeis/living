package com.hspedu.hspliving.commodity.exception;

import com.hspedu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 */
@ControllerAdvice(basePackages = "com.hspedu.hspliving.commodity.controller") //指定对哪个包进行全局异常处理
@Slf4j //允许日志输出
@ResponseBody //以json格式返回信息
public class HsplivingExceptionControllerAdvice {
    @ExceptionHandler({MethodArgumentNotValidException.class}) //指定对哪些异常进行处理(MethodArgument..数据校验异常)
    public R handleValidException(MethodArgumentNotValidException e) {
        //得到BindingResult对象，因为该对象存储了异常信息
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        //遍历bindingResult，将错误信息存放到Map集合
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(HsplivingCodeEnum.INVALID_EXCEPTION.getCode(),HsplivingCodeEnum.INVALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    //该方法处理handleValidException()没有精准匹配到的其他所有异常
    @ExceptionHandler({Throwable.class})
    public R handleException() {
        return R.error(HsplivingCodeEnum.UNKNOWN_EXCEPTION.getCode(),HsplivingCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
