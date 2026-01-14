package hello.boassebackend.global.util;

import hello.boassebackend.domain.notice.entity.Attachment;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir:uploads}")
    private String dirName;

    private String fileDir;

    @PostConstruct
    public void init() {
        // 프로젝트 루트 경로(user.dir)를 기준으로 uploads 폴더의 절대 경로를 생성합니다.
        // 예: C:/BoasseProject/boasse-backend/uploads/
        this.fileDir = System.getProperty("user.dir") + File.separator + dirName + File.separator;
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public Attachment storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        long size = multipartFile.getSize();

        // 디렉토리 생성 (없으면 생성)
        File directory = new File(fileDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created && !directory.exists()) {
                 throw new IOException("파일 업로드 디렉토리를 생성할 수 없습니다: " + fileDir);
            }
        }
        
        // 파일 저장 (절대 경로 사용)
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        // URL은 다운로드용 경로
        String url = "/api/v1/files/" + storeFileName; 

        return Attachment.builder()
                .originalName(originalFilename)
                .filename(storeFileName)
                .size(size)
                .url(url)
                .build();
    }

    // 물리 파일 삭제
    public void deleteFile(String filename) {
        File file = new File(getFullPath(filename));
        if (file.exists()) {
            file.delete();
        }
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        if (pos == -1) return ""; // 확장자 없는 경우 처리
        return originalFilename.substring(pos + 1);
    }
}
