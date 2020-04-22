package com.lixiang.controller;

import com.alibaba.druid.util.StringUtils;
import com.lixiang.controller.viewobject.UserVO;
import com.lixiang.error.BussinessException;
import com.lixiang.error.EmBusinessError;
import com.lixiang.response.CommonReturnType;
import com.lixiang.service.UserService;
import com.lixiang.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Controller 用于标记在一个类上，使用它标记的类就是一个SpringMVC Controller 对象。
 * 分发处理器将会扫描使用了该注解的类的方法，并检测该方法是否使用了@RequestMapping 注解。
 * @Controller 只是定义了一个控制器类，而使用@RequestMapping 注解的方法才是真正处理请
 * 求的处理器
 * */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @Autowired
    //内部有个threadlocalmap，使得每个线程都有对应的request
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value="/login",method={RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone")String telphone,
                                  @RequestParam(name="password")String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //参数校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)||
                StringUtils.isEmpty(password)){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录
        UserModel userModel = userService.validataLogin(telphone, this.EncodeByMd5(password));
        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value="/register",method={RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType regist(@RequestParam(name="telphone")String telphone,
                                   @RequestParam(name="otpCode") String otpCode,
                                   @RequestParam(name="name")String name,
                                   @RequestParam(name="gender")Integer gender,
                                   @RequestParam(name="age")Integer age,
                                   @RequestParam(name="password")String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
    //验证手机号和对应的otpcode相符合
        String inSessionOtpCode=(String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(inSessionOtpCode,otpCode)){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
    //用户注册流程
        UserModel userModel=new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setThirdPartyId("qq");
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();
        //加密字符串
        String newstr=base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

   //用户获取otp短信接口进行注册
    @RequestMapping(value="/getotp",method={RequestMethod.POST},consumes ={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt +=10000;
        String otpCode =String.valueOf(randomInt);
        //将OTP验证码同对应用户的手机号关联
        httpServletRequest.getSession().setAttribute(telphone,otpCode);
        //将OTP验证码通过短信发送给用户
        System.out.println("telphone="+telphone+"&otpCode="+otpCode);
        return CommonReturnType.create(null);
    }

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
