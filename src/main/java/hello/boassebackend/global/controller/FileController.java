package hello.boassebackend.global.controller;

import hello.boassebackend.domain.notice.entity.Attachment;
import hello.boassebackend.domain.notice.repository.AttachmentRepository;
import hello.boassebackend.global.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;
    private final AttachmentRepository attachmentRepository;

    @GetMapping("/api/v1/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, jakarta.servlet.http.HttpServletRequest request) throws MalformedURLException {
        // 1. 파일 경로 조회
        String fullPath = fileStore.getFullPath(filename);
        UrlResource resource = new UrlResource("file:" + fullPath);

        // 2. 파일 존재 여부 확인
        if (!resource.exists() || !resource.isReadable()) {
            log.warn("파일 다운로드 실패: filename={}, error=FileNotFoundException", filename);
            return ResponseEntity.notFound().build();
        }

        // 3. 원본 파일명 및 크기 조회 (DB)
        Attachment attachment = attachmentRepository.findByFilename(filename).orElse(null);
        String downloadName = (attachment != null) ? attachment.getOriginalName() : filename;
        long size = (attachment != null) ? attachment.getSize() : 0; // DB에 없으면 0으로 처리하거나 파일채널로 읽어야 함

        // 대용량 파일 로그 (100MB 이상)
        if (size > 100 * 1024 * 1024) {
            log.info("대용량 파일 다운로드 시작: filename={}, size={}MB", downloadName, size / (1024 * 1024));
        }
        
        // Interceptor로 로그 정보 전달
        String sizeStr = (size > 1024) ? (size / 1024) + "KB" : size + "B";
        if (size > 1024 * 1024) sizeStr = (size / (1024 * 1024)) + "MB";
        request.setAttribute("logData", "originalName=" + downloadName + ", size=" + sizeStr);

        // 4. 다운로드 헤더 설정 (한글 파일명 인코딩)
        String encodedUploadFileName = UriUtils.encode(downloadName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
