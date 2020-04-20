package com.lixiang.error;

public enum EmBusinessError implements CommonError  {
    //定义通用错误类型00001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),//逗号
    //10000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    UNKNOWN_ERROR(20002,"未知错误"),
    USER_LOGIN_FAIL(30000,"用户名或密码错误")
    ;
    private int errCode;
    private String errMsg;
    private EmBusinessError(int errCode,String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }


    //为了实现：对于同一个errorcode，有不同的errormsg
    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
