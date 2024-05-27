package com.zhouxiaoxuan.service;

import com.zhouxiaoxuan.entity.UserProfile;
import com.zhouxiaoxuan.mapper.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileMapper userProfileMapper;

    public UserProfile getUserProfileByUserId(int userId) {
        UserProfile userProfile = userProfileMapper.getUserProfileByUserId(userId);
        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUserId(userId);
            System.out.println("在UserProfileService中的userId为"+userId);
            userProfile.setGender("男");
            userProfileMapper.insertUserProfile(userProfile);
        }
        return userProfile;
    }

    @Transactional
    public void updateUserProfile(UserProfile userProfile) {
        userProfileMapper.updateUserProfile(userProfile);
    }

    @Transactional
    public void deleteUserProfileByUserId(int userId) {
        userProfileMapper.deleteUserProfileByUserId(userId);
    }

    public void updateAvatarUrl(Integer userId, String avatarUrl) {
        userProfileMapper.updateAvatarUrl(userId, avatarUrl);
    }

    // 获取所有user_profile表格中的数据
    public List<UserProfile> getAllUserProfiles() {
        return userProfileMapper.getAllUserProfiles();
    }
}
