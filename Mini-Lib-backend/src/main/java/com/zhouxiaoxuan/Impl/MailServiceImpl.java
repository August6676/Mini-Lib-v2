package com.zhouxiaoxuan.Impl;

import com.zhouxiaoxuan.entity.User;
import com.zhouxiaoxuan.mapper.UserMapper;
import com.zhouxiaoxuan.service.MailService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class MailServiceImpl implements MailService {

    @Value("631333748@qq.com")
    String from;

    @Resource
    UserMapper userMapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 发送验证码
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount){
    // 根据传入的邮箱和会话ID生成唯一的 key，用于标识该邮件地址和会话
        String key = "email:" + sessionId + ":" + email + ":" +hasAccount;
        // 如果该key的验证码还在有效期内，那么请求频繁，返回错误信息
        if(Boolean.TRUE.equals(template.hasKey(key))) {
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if(expire > 120) return "请求频繁，请稍后再试";
        }
        // 根据邮件地址查找用户信息，判断是否存在该用户
        User user = userMapper.findAccountByNameOrEmail(email);
        if(hasAccount && user == null) return "没有此邮件地址的账户";
        if(!hasAccount && user != null) return "此邮箱已被其他用户注册";
        // 生成一个六位数的随机验证码，有效期为三分钟
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的验证邮件");
        message.setText("Mini-Lib验证码是："+code);
        try {
            mailSender.send(message);
            template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "邮件发送失败，请坚持邮件地址是否有效";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId){
        // 检查 Redis 中是否存在该 key
        String key = "email:" + sessionId + ":" + email + ":false";
        if(Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if(s == null) return "验证码失效，请重新请求";
            // 用户名是否被注册
            if(s.equals(code)) {
                User user = userMapper.findAccountByNameOrEmail(username);
                if(user != null) return "此用户名已被注册，请更换用户名";
                template.delete(key);
                password = encoder.encode(password);
                User newUser = new User(username,password,email);
                // 创建用户账户
                if (userMapper.createAccount(newUser) > 0) {
                    return null;
                } else {
                    return "内部错误，请联系管理员";
                }
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":true";
        if(Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if(s == null) return "验证码失效，请重新请求";
            if(s.equals(code)) {
                template.delete(key);
                return null;
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password); // 使用 BCrypt 加密密码
        return userMapper.resetPasswordByEmail(password, email) > 0; // 重置密码
    }


}
