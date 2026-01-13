package hello.boassebackend.dto.notice;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NoticeUpdateRequest {
    private String title;
    private String content;
    private String password; // 관리자 비밀번호
    private List<MultipartFile> files;
    
    // 삭제할 파일 ID 리스트 (쉼표로 구분된 문자열로 들어오는 경우 처리)
    // List<Long>으로 바로 받을 수도 있지만, 명세서상 string (쉼표 구분)일 수 있으므로 유연하게 처리 필요.
    // 여기서는 명세서의 type: string description: "삭제할 파일 ID 리스트 (쉼표 구분)"을 따름
    private String removeFileIds;
}
