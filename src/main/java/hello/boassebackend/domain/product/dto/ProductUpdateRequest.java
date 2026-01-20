package hello.boassebackend.domain.product.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductUpdateRequest {
    private String title;
    private String category;
    private MultipartFile image;
    private String imageUrl; // 외부 이미지 URL 지원
    private String description;
    private String detail;
    private String specs; // JSON String
    private String features; // JSON String
    private Boolean isMainFeatured;
}
