package hello.boassebackend.domain.notice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NoticeCreateRequest {
    // multipart/form-data 요청을 바인딩하기 위한 DTO
    // Validation 어노테이션은 필요시 추가 (ex: @NotBlank)
    
    private String title;
    private String content;
    private String password; // 관리자 비밀번호
    private List<MultipartFile> files;
}