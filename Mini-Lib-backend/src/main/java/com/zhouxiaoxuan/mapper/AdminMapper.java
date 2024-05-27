package com.zhouxiaoxuan.mapper;

import com.zhouxiaoxuan.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    // 获取account表格中所有非管理员用户信息,且不获取密码
    @Select("SELECT id, email, gender, username FROM account WHERE username != 'admin'")
    List<User> getAllAccounts();

    // 新建用户，初始化password为123456
    @Insert("INSERT INTO account (username, email, password) VALUES (#{username},  #{email}, #{password})")
    boolean createUser(@Param("username") String username, @Param("email") String email, @Param("password") String password);

    // 删除用户信息
    @Delete("DELETE FROM account WHERE id = #{id}")
    void deleteUser(@Param("id") int id);

    // 修改用户信息
    @Update("UPDATE account SET username = #{username}, email = #{email} WHERE id = #{id}")
    void updateUser(@Param("id") int id, @Param("username") String username, @Param("email") String email);

    // 根据用户名获取用户ID
    @Select("SELECT id FROM account WHERE username = #{username}")
    int getIdByUsername(@Param("username") String username);
}
