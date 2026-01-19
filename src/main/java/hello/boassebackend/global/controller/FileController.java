package hello.boassebackend.global.controller;

import hello.boassebackend.domain.notice.entity.Attachment;
import hello.boassebackend.domain.notice.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final AttachmentRepository attachmentRepository;

    @GetMapping("/api/v1/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, jakarta.servlet.http.HttpServletRequest request) {
        
        // 1. DB에서 파일 정보 조회
        Attachment attachment = attachmentRepository.findByFilename(filename).orElse(null);

        // 2. 파일 존재 여부 확인
        if (attachment == null || attachment.getFileData() == null) {
            log.warn("파일 다운로드 실패: filename={}, error=NotFoundInDB", filename);
            return ResponseEntity.notFound().build();
        }

        long size = attachment.getSize();
        String downloadName = attachment.getOriginalName();
        byte[] fileData = attachment.getFileData();

        // 대용량 파일 로그 (100MB 이상)
        if (size > 100 * 1024 * 1024) {
            log.info("대용량 파일 다운로드 시작: filename={}, size={}MB", downloadName, size / (1024 * 1024));
        }
        
        // Interceptor로 로그 정보 전달
        String sizeStr = (size > 1024) ? (size / 1024) + "KB" : size + "B";
        if (size > 1024 * 1024) sizeStr = (size / (1024 * 1024)) + "MB";
        request.setAttribute("logData", "originalName=" + downloadName + ", size=" + sizeStr);

        // 3. 다운로드 헤더 설정 (한글 파일명 인코딩)
        String encodedUploadFileName = UriUtils.encode(downloadName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        
        // 4. ByteArrayResource로 데이터 반환
        ByteArrayResource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentLength(size) // 명시적 길이 설정
                .body(resource);
    }
}