package com.lixiang.error;

//包装器业务异常类实现
public class BussinessException extends Exception implements CommonError {

    private  CommonError commonError;

    //直接接受EmBussinessError的传参用于构造业务异常
    public BussinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }
    //接受自定义ErrMsg构造业务异常
    public BussinessException(CommonError commonError,String errMsg){
        super();
        this.commonError=commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String ErrMsg) {
        this.commonError.setErrMsg(ErrMsg);
        return this;
    }
}
