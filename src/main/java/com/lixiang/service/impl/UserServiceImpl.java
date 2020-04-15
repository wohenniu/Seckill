package com.lixiang.service.impl;

import com.lixiang.dao.UserDOMapper;
import com.lixiang.dao.UserPasswordDOMapper;
import com.lixiang.dataobject.UserDO;
import com.lixiang.dataobject.UserPasswordDO;
import com.lixiang.service.UserService;
import com.lixiang.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Override
    public UserModel getUserById(Integer id) {
        //调用userdaomapper获取到对应的用户dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){return null;}
        //通过用户id得到加密的密码信息
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }

    private UserModel convertFromDataObject(UserDO userDO ,UserPasswordDO userPasswordDo){
        if(userDO==null){
            return null;
        }
        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDo!=null){ userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());}

        return userModel;
    }
}
