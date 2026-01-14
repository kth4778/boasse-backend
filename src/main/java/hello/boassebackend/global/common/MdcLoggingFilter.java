package hello.boassebackend.global.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MdcLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 1. RequestID 생성 (헤더에 있으면 사용, 없으면 생성)
        // MSA 환경이나 로드밸런서를 탈 때 X-Request-ID가 넘어오는 경우가 많음
        String requestId = httpRequest.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString().substring(0, 8);
        }

        // 2. UserID 추출 (실제로는 세션/토큰에서 추출. 여기선 예시로 헤더나 임시값 사용)
        String userId = httpRequest.getHeader("X-User-ID"); 
        if (userId == null) {
            userId = "Anonymous";
        }

        // 3. MDC에 등록
        MDC.put("requestId", requestId);
        MDC.put("userId", userId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // 필수: ThreadLocal 정리
        }
    }
}