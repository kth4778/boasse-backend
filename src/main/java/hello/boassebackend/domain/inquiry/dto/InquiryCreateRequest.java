package hello.boassebackend.domain.inquiry.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InquiryCreateRequest {
    private String name;
    private String email;
    private String message;
    private Boolean isAgreed;
}
