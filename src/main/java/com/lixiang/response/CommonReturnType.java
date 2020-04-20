package com.lixiang.response;

public class CommonReturnType {
    //表明对应请求的返回结果：success或fail
    private String status;
    //若status等于false，使用通用的错误码格式
    //若status等于success，则data内返回前端需要的json数据
    /**
     * 在 JS 语言中，一切都是对象。因此，任何支持的类型都可以通过 JSON 来表示，
     * 例如字符串、数字、对象、数组等。但是对象和数组是比较特殊且常用的两种类型:
     *对象表示为键值对   数据由逗号分隔  花括号保存对象  方括号保存数组
     */
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create( result,"success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType commonReturnType=new CommonReturnType();
        commonReturnType.setData(result);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
