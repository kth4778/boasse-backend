package hello.boassebackend.domain.recruit.controller;

import hello.boassebackend.domain.recruit.dto.RecruitCreateRequest;
import hello.boassebackend.domain.recruit.dto.RecruitDetailResponse;
import hello.boassebackend.domain.recruit.dto.RecruitListResponse;
import hello.boassebackend.domain.recruit.service.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/recruits")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    /**
     * 채용공고 목록 조회
     */
    @GetMapping
    public ResponseEntity<RecruitListResponse> getRecruits(jakarta.servlet.http.HttpServletRequest request) {
        RecruitListResponse response = recruitService.getRecruits();
        return ResponseEntity.ok(response);
    }

    /**
     * 채용공고 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruitDetailResponse> getRecruit(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
        RecruitDetailResponse response = recruitService.getRecruitDetail(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 채용공고 작성
     */
    @PostMapping
    public ResponseEntity<RecruitDetailResponse> createRecruit(@RequestBody RecruitCreateRequest requestDto, jakarta.servlet.http.HttpServletRequest request) {
        Long recruitId = recruitService.createRecruit(requestDto);
        RecruitDetailResponse response = recruitService.getRecruitDetail(recruitId);
        
        request.setAttribute("logData", "recruitId=" + recruitId + ", title=" + requestDto.getTitle());
        return ResponseEntity.created(URI.create("/api/v1/recruits/" + recruitId))
                .body(response);
    }

    /**
     * 채용공고 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecruitDetailResponse> updateRecruit(@PathVariable Long id, @RequestBody RecruitCreateRequest requestDto, jakarta.servlet.http.HttpServletRequest request) {
        Long recruitId = recruitService.updateRecruit(id, requestDto);
        RecruitDetailResponse response = recruitService.getRecruitDetail(recruitId);

        request.setAttribute("logData", "recruitId=" + recruitId);
        return ResponseEntity.ok(response);
    }

    /**
     * 채용공고 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRecruit(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
        recruitService.deleteRecruit(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "삭제되었습니다.");

        request.setAttribute("logData", "recruitId=" + id);
        return ResponseEntity.ok(response);
    }
}