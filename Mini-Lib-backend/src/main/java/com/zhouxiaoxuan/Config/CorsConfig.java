package com.zhouxiaoxuan.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:5173/")// 设置允许跨域请求的域名
                .allowedHeaders("*")// 设置允许的请求头
                .allowCredentials(true)// 是否允许证书
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")// 允许的方法
                .maxAge(3600);// 跨域允许时间
    }
}


