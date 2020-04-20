package com.lixiang.dao;

/**
 * dao层：dao层叫数据访问层，全称为data access object，
 * 属于一种比较底层，比较基础的操作，具体到对于某个表、某个实体的增删改查
 * service层：service层叫服务层，被称为服务，肯定是相比之下比较高层次的一层结构，
 * 相当于将几种操作封装起
 * */
import com.lixiang.dataobject.UserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    UserDO selectByTelphone(String telphone);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}