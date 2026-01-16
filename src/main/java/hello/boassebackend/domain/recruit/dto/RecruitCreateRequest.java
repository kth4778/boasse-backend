package hello.boassebackend.domain.recruit.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecruitCreateRequest {
    private String title;
    private String status;
    private String location;
    private String type;
    private String applyLink;
    private List<String> recruit_duties;
    private List<String> recruit_requirements;
}