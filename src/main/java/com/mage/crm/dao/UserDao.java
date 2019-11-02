package com.mage.crm.dao;

import com.mage.crm.dto.UserDto;
import com.mage.crm.query.UserQuery;
import com.mage.crm.vo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户的数据库操作接口层
 */
public interface UserDao {
    /**
     * 根据用户id查询，返回用户
     * @param userId
     * @return
     */
    User queryUserById(String userId);

    /**
     * 根据用户名查询，返回用户
     * @param userName
     * @return
     */
    User queryUserByName(String userName);

    /**
     * 更新用户密码
     * @param userId
     * @param userPwd
     * @return
     */
    Integer updatePwd(@Param("id") String userId,@Param("userPwd") String userPwd);

    /**
     * 查询分配人
     * @return
     */
    List<User> queryAllCustomerManager();

    List<UserDto> queryUsersByParams(UserQuery userQuery);

    int insert(User user);

    int update(User user);

    int delete(Integer id);
}
