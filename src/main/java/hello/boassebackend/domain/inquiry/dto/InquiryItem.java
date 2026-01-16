package hello.boassebackend.domain.inquiry.dto;

import hello.boassebackend.domain.inquiry.entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryItem {
    private Long id;
    private String name;
    private String email;
    private String message;
    private Boolean isAgreed;
    private LocalDateTime createdAt;

    public static InquiryItem from(Inquiry inquiry) {
        return InquiryItem.builder()
                .id(inquiry.getId())
                .name(inquiry.getName())
                .email(inquiry.getEmail())
                .message(inquiry.getMessage())
                .isAgreed(inquiry.getIsAgreed())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
