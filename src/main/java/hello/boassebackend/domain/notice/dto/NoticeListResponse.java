package hello.boassebackend.domain.notice.dto;

import hello.boassebackend.global.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListResponse {
    private boolean success;
    private Data data;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private List<NoticeItem> notices;
        private Pagination pagination;
    }
}