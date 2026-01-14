package hello.boassebackend.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        // 필요하다면 여기서 요청 로그를 남길 수도 있음
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 비즈니스 로직 수행 후, 뷰 렌더링 전
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();
        
        // 컨트롤러에서 넘겨준 추가 정보 (예: originalName=file.png, size=15KB)
        String extraInfo = (String) request.getAttribute("logData");
        String logSuffix = (extraInfo != null) ? " | " + extraInfo : "";
        
        if (ex != null) {
            log.error("{} {} - {} Failed: {} ({}ms){}", method, uri, status, ex.getClass().getSimpleName(), duration, logSuffix);
        } else {
            // 400번대 이상 에러 응답인 경우 ERROR 레벨로, 정상인 경우 INFO 레벨로
            if (status >= 400) {
                 log.error("{} {} - {} ({}ms){}", method, uri, status, duration, logSuffix);
            } else {
                 log.info("{} {} - {} OK ({}ms){}", method, uri, status, duration, logSuffix);
            }
        }
    }
}
