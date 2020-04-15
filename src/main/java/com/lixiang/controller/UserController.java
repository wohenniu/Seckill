package com.lixiang.controller;

import com.lixiang.controller.viewobject.UserVO;
import com.lixiang.error.BussinessException;
import com.lixiang.error.EmBusinessError;
import com.lixiang.response.CommonReturnType;
import com.lixiang.service.UserService;
import com.lixiang.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("user")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

   @RequestMapping("/get")
   @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BussinessException {
      //调用service服务获取对应id的用户对象并返回给前端
       UserModel userModel=userService.getUserById(id);

       //若获取的用户信息不存在
       if(userModel==null){
           throw new BussinessException(EmBusinessError.USER_NOT_EXIST);
       }

       //讲核心领域模型用户对象转化为可供ui使用的viewobject
       UserVO userVO= convertFromModel(userModel);
      //返回通用对象
       return CommonReturnType.create(userVO);
   }
   private UserVO convertFromModel(UserModel userModel){
       if(userModel==null){return null;}
       UserVO userVO=new UserVO();
       BeanUtils.copyProperties(userModel,userVO);
       return userVO;
 }

}
