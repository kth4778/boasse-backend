package hello.boassebackend.domain.inquiry.dto;

import hello.boassebackend.global.common.Pagination;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InquiryListResponse {
    private boolean success;
    private InquiryData data;

    @Data
    @Builder
    public static class InquiryData {
        private List<InquiryItem> inquiries;
        private Pagination pagination;
    }
}
