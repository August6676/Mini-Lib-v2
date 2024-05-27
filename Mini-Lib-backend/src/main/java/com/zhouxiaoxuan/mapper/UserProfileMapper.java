package com.zhouxiaoxuan.mapper;

import com.zhouxiaoxuan.entity.UserProfile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserProfileMapper {

    // 根据用account表中的id获取user_profile表中的信息，如果不存在则新建一个表格
    @Select("SELECT * FROM user_profile WHERE user_id = #{userId}")
    UserProfile getUserProfileByUserId(@Param("userId") int userId);

    @Insert("INSERT INTO user_profile (user_id, age, gender, bio, avatar_url) VALUES (#{userId}, #{age}, #{gender}, #{bio}, #{avatarUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUserProfile(UserProfile userProfile);

    // 更新user_profile表中的信息
    @Update("UPDATE user_profile SET age = #{age}, gender = #{gender}, bio = #{bio}, avatar_url = #{avatarUrl} WHERE user_id = #{userId}")
    void updateUserProfile(UserProfile userProfile);

    // 根据id删除user_profile表格中user_id为对应id的数据
    @Delete("DELETE FROM user_profile WHERE user_id = #{userId}")
    void deleteUserProfileByUserId(@Param("userId") int userId);

    // 新增的方法
    @Update("UPDATE user_profile SET avatar_url = #{avatarUrl} WHERE user_id = #{userId}")
    void updateAvatarUrl(@Param("userId") Integer userId, @Param("avatarUrl") String avatarUrl);

    // 获取所有user_profile表格中的数据
    @Select("SELECT * FROM user_profile")
    List<UserProfile> getAllUserProfiles();
}
