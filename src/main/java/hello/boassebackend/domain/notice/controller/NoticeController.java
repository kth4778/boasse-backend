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

    /**
     * 공지사항 목록 조회 (페이징 적용)
     */
    @GetMapping
    public ResponseEntity<NoticeListResponse> getNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            jakarta.servlet.http.HttpServletRequest request) {
        
        NoticeListResponse response = noticeService.getNotices(page, limit);
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
        String clientIp = request.getRemoteAddr();
        
        if (!isVisited) {
            // 처음 방문: 조회수 증가 (동시 요청 방어 로직 포함)
            responseDto = noticeService.getNoticeDetailWithViewCount(id, clientIp);

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

        request.setAttribute("logData", "noticeId=" + id + ", visited=" + isVisited + ", ip=" + clientIp);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 공지사항 작성
     */
    @PostMapping
    public ResponseEntity<NoticeDetailResponse> createNotice(
            @ModelAttribute NoticeCreateRequest requestDto,
            jakarta.servlet.http.HttpServletRequest request) throws IOException {
        
        Long noticeId = noticeService.createNotice(requestDto, "Admin");
        
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);
        
        request.setAttribute("logData", "noticeId=" + noticeId + ", title=" + requestDto.getTitle());
        return ResponseEntity.created(URI.create("/api/v1/notices/" + noticeId))
                .body(response);
    }

    /**
     * 공지사항 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> updateNotice(
            @PathVariable Long id,
            @ModelAttribute NoticeUpdateRequest requestDto,
            jakarta.servlet.http.HttpServletRequest request) throws IOException {
        
        Long noticeId = noticeService.updateNotice(id, requestDto);
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);

        request.setAttribute("logData", "noticeId=" + noticeId);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotice(
            @PathVariable Long id,
            jakarta.servlet.http.HttpServletRequest request) { 

        noticeService.deleteNotice(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "삭제되었습니다.");
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("deletedAt", LocalDateTime.now());
        response.put("data", data);

        request.setAttribute("logData", "noticeId=" + id);
        return ResponseEntity.ok(response);
    }
}