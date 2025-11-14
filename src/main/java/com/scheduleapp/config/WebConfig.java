package com.scheduleapp.config;

import com.scheduleapp.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)  // 인터셉터 실행 순서
                .addPathPatterns("/**")  // 모든 경로에 적용
                .excludePathPatterns(
                        // 인증 체크 제외 경로
                        "/api/auth/signup",    // 회원가입
                        "/api/auth/login",     // 로그인
                        "/error"               // 에러 페이지
                );
    }
}
