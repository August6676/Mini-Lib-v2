package com.zhouxiaoxuan.service;

public interface MailService {
    String sendValidateEmail(String email, String sessionId, boolean hasAccount); // 发送验证码
    String validateAndRegister(String username, String password, String email, String code, String sessionId); // 验证并注册
    String validateOnly(String email, String code, String sessionId); // 验证邮箱
    boolean resetPassword(String password, String email); // 重置密码
}
