package com.leili.config;

import com.leili.interceptor.TokenRefreshInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenRefreshInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/usr/login")
                .excludePathPatterns("/usr/insert-in-login-page");
    }
}
