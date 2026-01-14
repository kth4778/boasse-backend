package hello.boassebackend.domain.notice.controller;

import hello.boassebackend.domain.notice.dto.NoticeCreateRequest;
import hello.boassebackend.domain.notice.dto.NoticeDetailResponse;
import hello.boassebackend.domain.notice.dto.NoticeListResponse;
import hello.boassebackend.domain.notice.dto.NoticeUpdateRequest;
import hello.boassebackend.global.exception.ForbiddenException;
import hello.boassebackend.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 관리자 비밀번호 (실제 운영 시에는 DB나 환경변수 관리 권장)
    private static final String ADMIN_PASSWORD = "admin1234";

    /**
     * 공지사항 목록 조회
     */
    @GetMapping
    public ResponseEntity<NoticeListResponse> getNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("게시물 목록 조회 요청: page={}, limit={}", page, limit);
        
        NoticeListResponse response = noticeService.getNotices(page, limit);
        
        log.info("GET /api/v1/notices - 200 OK");
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 상세 조회
     * 쿠키를 사용하여 조회수 중복 증가 방지
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> getNotice(
            @PathVariable Long id,
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response) {

        log.info("게시물 조회 요청: noticeId={}", id);

        String cookieName = "view_log_" + id;
        boolean isVisited = false;

        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    isVisited = true;
                    break;
                }
            }
        }

        NoticeDetailResponse responseDto;
        if (!isVisited) {
            // 처음 방문: 조회수 증가
            responseDto = noticeService.getNoticeDetailWithViewCount(id);

            // 쿠키 생성 (오늘 자정까지 유지)
            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie(cookieName, "true");
            cookie.setPath("/");
            
            // 오늘 자정까지 남은 초 계산
            long todayEndSecond = java.time.LocalDate.now().atTime(java.time.LocalTime.MAX)
                    .toEpochSecond(java.time.ZoneOffset.ofHours(9));
            long currentSecond = java.time.LocalDateTime.now()
                    .toEpochSecond(java.time.ZoneOffset.ofHours(9));
            
            cookie.setMaxAge((int) (todayEndSecond - currentSecond));
            response.addCookie(cookie);
        } else {
            // 재방문: 조회수 증가 X
            responseDto = noticeService.getNoticeDetail(id);
        }

        log.info("GET /api/v1/notices/{} - 200 OK", id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 공지사항 작성 (관리자 전용 - 비밀번호 체크)
     */
    @PostMapping
    public ResponseEntity<NoticeDetailResponse> createNotice(
            @ModelAttribute NoticeCreateRequest request) throws IOException {
        
        int fileCount = (request.getFiles() != null) ? request.getFiles().size() : 0;
        log.info("게시물 작성 요청: title=\"{}\", attachmentCount={}", request.getTitle(), fileCount);
        
        checkPassword(request.getPassword());

        Long noticeId = noticeService.createNotice(request, "Admin");
        
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);
        
        log.info("POST /api/v1/notices - 201 CREATED");
        return ResponseEntity.created(URI.create("/api/v1/notices/" + noticeId))
                .body(response);
    }

    /**
     * 공지사항 수정 (관리자 전용 - 비밀번호 체크)
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> updateNotice(
            @PathVariable Long id,
            @ModelAttribute NoticeUpdateRequest request) throws IOException {
        
        log.info("게시물 수정 요청: noticeId={}", id);
        
        checkPassword(request.getPassword());

        Long noticeId = noticeService.updateNotice(id, request);
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);

        log.info("PUT /api/v1/notices/{} - 200 OK", id);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 삭제 (관리자 전용 - 비밀번호 체크)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotice(
            @PathVariable Long id,
            @RequestParam String password) { // 쿼리 파라미터로 비밀번호 수신
        
        log.info("게시물 삭제 요청: noticeId={}", id);
        
        checkPassword(password);

        noticeService.deleteNotice(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "삭제되었습니다.");
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("deletedAt", LocalDateTime.now());
        response.put("data", data);

        log.info("DELETE /api/v1/notices/{} - 200 OK", id);
        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 검증 메서드
     */
    private void checkPassword(String password) {
        // 디버깅을 위해 로그 출력 (운영 환경에서는 비밀번호 로깅 주의 - 마스킹 필요하나 여기선 생략)
        // log.debug("Received password check"); 
        
        if (password == null || !ADMIN_PASSWORD.equals(password.trim())) {
            log.warn("권한 없는 접근 시도 (비밀번호 불일치)");
            throw new ForbiddenException("관리자 비밀번호가 일치하지 않습니다.");
        }
    }
}
