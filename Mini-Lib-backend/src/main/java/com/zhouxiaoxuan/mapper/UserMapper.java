package com.zhouxiaoxuan.mapper;

import com.zhouxiaoxuan.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // 查找用户
    @Select("select * from account where username = #{text} or email = #{text}")
    User findAccountByNameOrEmail(String text);

    // 创建新用户
    @Insert("insert into account (email, username, password) values (#{email}, #{username}, #{password})")
    int createAccount(User user);


    // 验证登录
    @Select("select * from account where username = #{username} and password = #{password}")
    User login(User user);

    // 修改密码
    @Update("update account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(@Param("password") String password, @Param("email") String email);

    // 修改用户信息
    @Update("update account set username = #{username}, email = #{email}, password = #{password} where id = #{id}")
    int updateUser(User user);
}
