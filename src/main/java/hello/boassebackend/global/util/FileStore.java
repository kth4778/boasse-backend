package hello.boassebackend.global.util;

import hello.boassebackend.domain.notice.entity.Attachment;
import hello.boassebackend.domain.notice.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStore {

    private final AttachmentRepository attachmentRepository;

    @Transactional
    public Attachment storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        long size = multipartFile.getSize();
        byte[] fileData = multipartFile.getBytes(); // 파일 데이터를 바이트 배열로 변환

        // URL은 다운로드용 경로
        String url = "/api/v1/files/" + storeFileName;

        Attachment attachment = Attachment.builder()
                .originalName(originalFilename)
                .filename(storeFileName)
                .size(size)
                .url(url)
                .fileData(fileData) // DB에 바이너리 데이터 저장
                .build();
        
        // DB에 즉시 저장하여 데이터 영속화 (Product 등 연관관계 없는 곳에서도 사용 가능하도록)
        return attachmentRepository.save(attachment);
    }

    // DB 데이터 삭제
    @Transactional
    public void deleteFile(String filename) {
        attachmentRepository.findByFilename(filename)
                .ifPresent(attachmentRepository::delete);
    }

    // 파일 경로 조회 메서드는 DB 저장 방식에서 의미가 없으므로 더미 반환하거나 제거
    public String getFullPath(String filename) {
        return "DB_STORAGE:" + filename;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        if (pos == -1) return "";
        return originalFilename.substring(pos + 1);
    }
}