package com.scheduleapp.interceptor;

import com.scheduleapp.common.SessionConst;
import com.scheduleapp.exception.BusinessException;
import com.scheduleapp.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    )  throws Exception {

        String requestURI = request.getRequestURI();
        log.info("Authentication check for URI: {}", requestURI);

        // 세션 조회
        HttpSession session = request.getSession(false);

        // 세션이 없거나 로그인 정모가 없는 경우
        if (session == null || session.getAttribute(SessionConst.LOGIN_USER_ID) == null) {
            log.warn("Unauthorized access attempt to: {}", requestURI);
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // 로그인된 유저 ID 로깅
        Long userId = (Long) session.getAttribute(SessionConst.LOGIN_USER_ID);
        log.info("Authenticated user: {}", userId);

        return true;  // 인증 성공, 다음 단계로 진행
    }
}
