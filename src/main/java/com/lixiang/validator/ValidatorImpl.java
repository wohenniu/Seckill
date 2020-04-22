package com.lixiang.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.xml.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private javax.validation.Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result=new ValidationResult();
        Set<ConstraintViolation<Object>> constriantViolatationSet = validator.validate(bean);
        if(constriantViolatationSet.size()>0){
            //有错误
            result.setHasErrors(true);
            constriantViolatationSet.forEach(constriantViolatation->{
                String errMsg=constriantViolatation.getMessage();
                String propertyName=constriantViolatation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName,errMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂初始化方式使其实例化
        this.validator=  Validation.buildDefaultValidatorFactory().getValidator();
    }
}
