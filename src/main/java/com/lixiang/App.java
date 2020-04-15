package com.lixiang;

import com.lixiang.dao.UserDOMapper;
import com.lixiang.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.lixiang"})
@RestController
@MapperScan("com.lixiang.dao")
public class App
{
    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
    public String home(){
        UserDO userDO=userDOMapper.selectByPrimaryKey(1);
        if(userDO==null){
            return "用户不存在";
        }else{
            return userDO.getName()+",你好，35万等着你";
        }

    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Integer i=1000;
        Integer j=1000;
        System.out.println(i==j);
        System.out.println(i.equals(j));
        SpringApplication.run(App.class,args);

    }

}
