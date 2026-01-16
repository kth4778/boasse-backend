package hello.boassebackend.domain.recruit.dto;

import hello.boassebackend.domain.recruit.entity.Recruit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitItem {
    private Long id;
    private String title;
    private String status;
    private String location;
    private String type;
    private String applyLink;
    private java.util.List<String> recruit_duties;
    private java.util.List<String> recruit_requirements;
    private java.time.LocalDateTime createdAt;

    public static RecruitItem from(hello.boassebackend.domain.recruit.entity.Recruit recruit) {
        return RecruitItem.builder()
                .id(recruit.getId())
                .title(recruit.getTitle())
                .status(recruit.getStatus())
                .location(recruit.getLocation())
                .type(recruit.getType())
                .applyLink(recruit.getApplyLink())
                .recruit_duties(recruit.getDuties())
                .recruit_requirements(recruit.getRequirements())
                .createdAt(recruit.getCreatedAt())
                .build();
    }
}