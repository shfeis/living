package com.hspedu.hspliving.commodity.exception;

/**
 * 规定状态码的枚举类
 */
public enum HsplivingCodeEnum {
    UNKNOWN_EXCEPTION(40000,"系统未知异常"),
    INVALID_EXCEPTION(40001,"参数校验异常");

    private int code; //状态码
    private String msg; //状态码说明，提示信息

    HsplivingCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
