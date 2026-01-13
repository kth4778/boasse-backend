package hello.boassebackend.exception;

import hello.boassebackend.dto.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 Bad Request / 404 Not Found (IllegalArgumentException 활용)
     * Service에서 리소스를 찾을 수 없을 때 IllegalArgumentException을 던지도록 구현했으므로 이를 404로 매핑합니다.
     * 상황에 따라 400으로 처리하기도 하지만, 여기서는 "찾을 수 없음"의 의미가 강해 404로 처리하거나
     * 별도의 NotFoundException을 만드는 것이 좋으나, 편의상 메시지에 따라 분기하거나 404로 통일합니다.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        // 메시지에 "찾을 수 없습니다"가 포함되면 404, 아니면 400 등으로 분기 가능.
        // 여기서는 명세서의 NotFoundError를 위해 404로 매핑합니다.
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("NOT_FOUND", e.getMessage()));
    }

    /**
     * 403 Forbidden
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        log.error("ForbiddenException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of("FORBIDDEN", e.getMessage()));
    }

    /**
     * 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."));
    }
}
