package com.zhouxiaoxuan.controller;

import com.zhouxiaoxuan.entity.User;
import com.zhouxiaoxuan.entity.UserProfile;
import com.zhouxiaoxuan.mapper.AdminMapper;
import com.zhouxiaoxuan.service.AdminService;
import com.zhouxiaoxuan.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true",allowedHeaders = "*",maxAge = 3600)
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    UserProfileService userProfileService;

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public void handleOptions() {
        // Do nothing and return OK status for pre-flight requests
    }

    // 获取所有用户信息 √
    @GetMapping("/users")
    public List<User> getAllAccounts() {
        System.out.println(adminService.getAllAccounts());
        return adminService.getAllAccounts();
    }

    // 创建用户 √
    @PostMapping("/users")
    public User createUser(@RequestParam String username, @RequestParam String email) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("123456");
        if (adminService.createUser(username, email,password)) {

            int id = adminService.getIdByUsername(username);
            UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
            return new User(id, username, email);
        } else {
            return null;
        }
    }

    // 删除用户 √
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {

        userProfileService.deleteUserProfileByUserId(id);
        adminService.deleteUser(id);

    }

    // 修改用户
    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable int id,
                           @RequestParam String username,
                           @RequestParam String email) {

        adminService.updateUser(id, username, email);
    }

    // 退出登录
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "退出登录成功");
        return response;
    }

    // 获取所有userProfile的信息 √
    @GetMapping("/userProfile")
    public Map<String, Object> getUserProfile() {
        List<UserProfile> userProfiles = userProfileService.getAllUserProfiles();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", userProfiles);
        return response;
    }

    // 根据id获取userProfile的信息
    @GetMapping("/userProfile/{id}")
    public UserProfile getUserProfileById(@PathVariable int id) {
        return userProfileService.getUserProfileByUserId(id);
    }

}
