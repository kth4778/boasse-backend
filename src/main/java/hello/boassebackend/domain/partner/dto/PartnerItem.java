package hello.boassebackend.domain.partner.dto;

import hello.boassebackend.domain.partner.entity.Partner;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PartnerItem {
    private Long id;
    private String name;
    private String link;
    private String logo;
    private LocalDateTime createdAt;

    public static PartnerItem from(Partner partner) {
        return PartnerItem.builder()
                .id(partner.getId())
                .name(partner.getName())
                .link(partner.getLink())
                .logo(partner.getLogo())
                .createdAt(partner.getCreatedAt())
                .build();
    }
}
