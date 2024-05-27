package com.zhouxiaoxuan.controller;

import com.zhouxiaoxuan.entity.User;
import com.zhouxiaoxuan.mapper.UserMapper;
import com.zhouxiaoxuan.service.MailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Mapper;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
public class AuthorizeController {

    // 用于验证电子邮件的正则表达式形式
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";
    // 验证用户姓名的正则表达式
    private final String USERNAME_REGEX = "^[a-zA-Z0-9一-龥]+$";

    @Autowired
    private MailService mailService;

    @Mapper
    @Autowired
    UserMapper userMapper;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/valid-register-email")
    public Map<String,Object> validateRegisterEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                        HttpSession session){
        Map<String, Object> response = new HashMap<>();
        String s = mailService.sendValidateEmail(email, session.getId(), false); // 发送验证邮件
        // 发送成功 返回成功的响应对象 否则返回失败的相应对象
        if (s == null){
            response.put("success", true);
            response.put("message", "邮件已发送，请注意查收");
        }
        else{
            response.put("success", false);
            response.put("message", "邮件发送失败");
        }
        return response;
    }

    @PostMapping("/valid-reset-email")
    public Map<String, Object> validateResetEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                     HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String s = mailService.sendValidateEmail(email, session.getId(), true); // 发送验证邮件
        // 发送成功 返回成功的响应对象 否则返回失败的相应对象
        if (s == null) {
            response.put("success", true);
            response.put("message", "邮件已发送，验证码三分钟内有效，请注意查收");
        } else {
            response.put("success", false);
            response.put("message", "邮件发送失败");
        }
        return response;
    }

    @PostMapping("/register")
    public Map<String,Object> registerUser(@Pattern(regexp = USERNAME_REGEX) @Length(min = 2, max = 8) @RequestParam("username") String username,
                               @Length(min = 6, max = 16) @RequestParam("password") String password,
                               @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                               @Length(min = 6, max = 6) @RequestParam("code") String code,
                               HttpSession session){
        Map<String, Object> response = new HashMap<>();
        String s = mailService.validateAndRegister(username, password, email, code, session.getId());
        if (s == null) {
            response.put("success", true);
            response.put("message", "注册成功");
            User user = userMapper.findAccountByNameOrEmail(username);
            session.setAttribute("user", user);
        } else {
            response.put("success", false);
            response.put("message", "注册失败"+s);
        }
        return response;
    }

    @PostMapping("/start-reset")
    public Map<String, Object> startReset(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                            @Length(min = 6, max = 6) @RequestParam("code") String code,
                            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String s = mailService.validateOnly(email,code,session.getId());
        if (s == null) {
            session.setAttribute("reset-password", email);
            response.put("success", true);
            response.put("message", "验证成功");
        } else {
            response.put("success", false);
            response.put("message", "出错了: " + s);
        }
        return response;
    }

    @PostMapping("/do-reset")
    public Map<String, Object> resetPassword(@Length(min = 6, max = 16) @RequestParam("password") String password,
                                HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String email = (String) session.getAttribute("reset-password");
        if (email == null) {
            response.put("success", false);
            response.put("message", "请先完成邮箱验证");
        } else if (mailService.resetPassword(password, email)) {
            session.removeAttribute("reset-password");
            response.put("success", true);
            response.put("message", "重置成功");
        } else {
            response.put("success", false);
            response.put("message", "重置失败");
        }
        return response;
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = userMapper.findAccountByNameOrEmail(username);
        if (user == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
        }
        else if (encoder.matches(password, user.getPassword())) {
            session.setAttribute("user", user);
            response.put("success", true);
            response.put("message", "登录成功");
        } else {
            response.put("success", false);
            response.put("message", "密码错误");
        }
        return response;
    }

}
