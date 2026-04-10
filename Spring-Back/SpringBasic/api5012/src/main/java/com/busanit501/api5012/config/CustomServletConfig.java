package com.busanit501.api5012.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("classpath:/static/");

        // 프로필 이미지 업로드 파일 서빙
        // /upload/{파일명} 요청 → c:/upload/springTest/ 폴더에서 파일 반환
        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations("file:c:/upload/springTest/");
    }
}