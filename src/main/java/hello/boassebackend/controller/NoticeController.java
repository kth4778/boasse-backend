package hello.boassebackend.controller;

import hello.boassebackend.dto.notice.NoticeCreateRequest;
import hello.boassebackend.dto.notice.NoticeDetailResponse;
import hello.boassebackend.dto.notice.NoticeListResponse;
import hello.boassebackend.dto.notice.NoticeUpdateRequest;
import hello.boassebackend.exception.ForbiddenException;
import hello.boassebackend.service.NoticeService;
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
        
        NoticeListResponse response = noticeService.getNotices(page, limit);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> getNotice(@PathVariable Long id) {
        NoticeDetailResponse response = noticeService.getNoticeDetail(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 작성 (관리자 전용 - 비밀번호 체크)
     */
    @PostMapping
    public ResponseEntity<NoticeDetailResponse> createNotice(
            @ModelAttribute NoticeCreateRequest request) throws IOException {
        
        checkPassword(request.getPassword());

        Long noticeId = noticeService.createNotice(request, "Admin");
        
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);
        
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
        
        checkPassword(request.getPassword());

        Long noticeId = noticeService.updateNotice(id, request);
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);

        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 삭제 (관리자 전용 - 비밀번호 체크)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotice(
            @PathVariable Long id,
            @RequestParam String password) { // 쿼리 파라미터로 비밀번호 수신
        
        checkPassword(password);

        noticeService.deleteNotice(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "삭제되었습니다.");
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("deletedAt", LocalDateTime.now());
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 검증 메서드
     */
    private void checkPassword(String password) {
        if (password == null || !ADMIN_PASSWORD.equals(password)) {
            throw new ForbiddenException("관리자 비밀번호가 일치하지 않습니다.");
        }
    }
}