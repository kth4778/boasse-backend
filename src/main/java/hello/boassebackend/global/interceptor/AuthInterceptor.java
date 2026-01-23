package hello.boassebackend.global.interceptor;

import hello.boassebackend.global.exception.ForbiddenException;
import hello.boassebackend.global.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 1. 인증이 필요 없는 공개 API (화이트리스트)
        // 로그인
        if (requestURI.equals("/api/v1/auth/login")) {
            return true;
        }
        // 문의 등록 (POST /api/v1/inquiries)
        if (method.equals("POST") && requestURI.startsWith("/api/v1/inquiries")) {
            return true;
        }
        // 그 외 GET 요청 중 인증이 필요한 것을 제외하고는 모두 허용
        // 인증 필요한 GET: /auth/verify, /inquiries/**
        if (method.equals("GET")) {
            if (!requestURI.startsWith("/api/v1/auth/verify") && !requestURI.startsWith("/api/v1/inquiries")) {
                return true;
            }
        }

        // 2. 위에서 걸러지지 않은 요청은 모두 인증 필요 (관리자 API)
        // (GET /inquiries, GET /auth/verify, POST/PUT/DELETE 그 외 모든 경로)
        
        String token = resolveToken(request);
        
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return true;
        }

        log.warn("인증 실패: {} {}", method, requestURI);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
