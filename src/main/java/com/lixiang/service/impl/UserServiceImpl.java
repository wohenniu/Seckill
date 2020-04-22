package com.lixiang.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.lixiang.dao.UserDOMapper;
import com.lixiang.dao.UserPasswordDOMapper;
import com.lixiang.dataobject.UserDO;
import com.lixiang.dataobject.UserPasswordDO;
import com.lixiang.error.BussinessException;
import com.lixiang.error.EmBusinessError;
import com.lixiang.service.UserService;
import com.lixiang.service.model.UserModel;
import com.lixiang.validator.ValidationResult;
import com.lixiang.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;
    @Override
    public UserModel getUserById(Integer id) {
        //调用userdaomapper获取到对应的用户dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){return null;}
        //通过用户id得到加密的密码信息
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }


    @Override
    @Transactional
    public void register(UserModel userModel) throws  BussinessException{
        if(userModel==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())
//                || userModel.getGender()==null
//                || userModel.getAge()==null
//                || StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }


        //实现model->dataobject方法
         UserDO userDO=convertFromModel(userModel);
         userDOMapper.insertSelective(userDO);
         userModel.setId(userDO.getId());
         UserPasswordDO userPasswordDO=convertPasswordFromModel(userModel);
         userPasswordDOMapper.insertSelective(userPasswordDO);
         return;
         }

    @Override
    public UserModel validataLogin(String telphone, String encrptPassword) throws BussinessException {
        //通过用户手机获取用户信息
        UserDO userDO=userDOMapper.selectByTelphone(telphone);
        if(userDO==null){
            throw new BussinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel=convertFromDataObject(userDO,userPasswordDO);
        //比对用户信息内加密的密码是否和传输进来的密码想匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())) {
            throw new BussinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO =new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserDO userDO =new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
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
