package com.dgut.config;

import com.dgut.interceptors.JwtInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
//                .addPathPatterns("/admin/**")
//                .addPathPatterns("/patient/**")
//                .addPathPatterns("/doctor/**")
                .addPathPatterns("/666")
                .excludePathPatterns("/patient/pdf")
                .excludePathPatterns("/**/login");
    }
}
