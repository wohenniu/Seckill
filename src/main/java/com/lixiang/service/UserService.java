package com.lixiang.service;

import com.lixiang.error.BussinessException;
import com.lixiang.service.model.UserModel;
//接口找到实现类快捷键：ctrl+alt+B
public interface UserService {
    //通过id获取用户对象
    UserModel getUserById(Integer id);
    void register (UserModel userModel) throws BussinessException;
    /**
     * telphone:用户手机
     * password：用户加密后的密码
     * */
    UserModel validataLogin(String telphone,String encrptPassword) throws BussinessException;
}
