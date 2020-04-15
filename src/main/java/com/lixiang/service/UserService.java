package com.lixiang.service;

import com.lixiang.service.model.UserModel;

public interface UserService {
    //通过id获取用户对象
    UserModel getUserById(Integer id);
}
