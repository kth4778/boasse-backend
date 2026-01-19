package hello.boassebackend.domain.partner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerListResponse {
    private boolean success;
    private Data data;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private List<PartnerItem> items;
    }
}
