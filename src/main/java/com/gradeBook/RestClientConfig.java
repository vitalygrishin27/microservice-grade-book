package com.gradeBook;

import com.gradeBook.interceptor.GradeBookInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestClientConfig implements WebMvcConfigurer {
    @Autowired
    private GradeBookInterceptor gradeBookInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gradeBookInterceptor);
    }
}