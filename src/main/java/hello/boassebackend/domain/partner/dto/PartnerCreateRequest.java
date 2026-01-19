package hello.boassebackend.domain.partner.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PartnerCreateRequest {
    private String name;
    private String link;
    private MultipartFile logo;
}
