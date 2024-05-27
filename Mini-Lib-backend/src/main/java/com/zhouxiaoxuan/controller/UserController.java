package com.zhouxiaoxuan.controller;

import com.zhouxiaoxuan.entity.User;
import com.zhouxiaoxuan.entity.UserProfile;
import com.zhouxiaoxuan.mapper.UserMapper;
import com.zhouxiaoxuan.service.AdminService;
import com.zhouxiaoxuan.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173/", maxAge = 3600)
public class UserController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserProfileService userProfileService;

    // 这个方法是为了获取用户的个人信息
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(HttpSession session) {
        // 从 session 中获取用户信息
        User user = (User) session.getAttribute("user");
        System.out.println(user.toString());

        return ResponseEntity.ok(user); // 返回用户信息
    }

    // 根据用户id获取用户的user_profile中的信息
    @GetMapping("/profile/details")
    public ResponseEntity<UserProfile> getUserProfileDetails(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println(user.getId());
        UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
        return ResponseEntity.ok(userProfile);
    }

    // 修改用户个人信息
    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(@RequestBody User user, HttpSession session) {
        // 从 session 中获取用户信息
        User user1 = (User) session.getAttribute("user");
        // 修改用户信息
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        // 更新用户信息
        userMapper.updateUser(user1);
        // 更新 session 中的用户信息
        session.setAttribute("user", user1);
        return ResponseEntity.ok(user1);
    }

    // 根据用户id修改用户的user_profile中的信息
    @PutMapping("/profile/details")
    public ResponseEntity<UserProfile> updateUserProfileDetails(@RequestBody UserProfile userProfile, HttpSession session) {
        User user = (User) session.getAttribute("user");
        userProfile.setUserId(user.getId());
        userProfileService.updateUserProfile(userProfile);
        return ResponseEntity.ok(userProfile);
    }

    @PostMapping("/profile/avatar")
    public ResponseEntity<String> updateAvatarUrl(@RequestParam("avatarUrl") String avatarUrl, HttpSession session) {
        User user = (User) session.getAttribute("user");
        userProfileService.updateAvatarUrl(user.getId(), avatarUrl);
        return ResponseEntity.ok("Avatar updated successfully");
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 修改密码
    @PutMapping("/validate-password")
    public ResponseEntity<String> updatePassword(@RequestParam("oldPassword") String oldPassword,
                                                 @RequestParam("newPassword") String newPassword,
                                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        System.out.println("用户的旧密码为" + user.getPassword());
        System.out.println("用户输入的旧密码为" + oldPassword);
        if (!encoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(400).body("Old password is incorrect");
        }
        System.out.println("用户的新密码为" + newPassword);
        System.out.println("加密后的新密码为" + encoder.encode(newPassword));
        System.out.println("用户的id为" + user.getId());
        user.setPassword(encoder.encode(newPassword));
        userMapper.updateUser(user);
        return ResponseEntity.ok("Password updated successfully");
    }

    // 退出登录
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        System.out.println("用户退出登录");
        session.invalidate(); // 清除session中的所有信息
        return ResponseEntity.ok("Logout successful");
    }
}
