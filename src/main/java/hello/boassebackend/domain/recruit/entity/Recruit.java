package hello.boassebackend.domain.recruit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recruit")
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 50)
    private String status; // 채용 상태 (예: 채용중, 마감)

    @Column(length = 100)
    private String location; // 근무지

    @Column(length = 50)
    private String type; // 고용 형태 (예: 정규직, 계약직)

    @Column(nullable = false)
    private String applyLink; // 지원 링크

    @ElementCollection
    @CollectionTable(name = "recruit_duties", joinColumns = @JoinColumn(name = "recruit_id"))
    @Column(name = "duty", columnDefinition = "TEXT")
    private List<String> duties = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recruit_requirements", joinColumns = @JoinColumn(name = "recruit_id"))
    @Column(name = "requirement", columnDefinition = "TEXT")
    private List<String> requirements = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Recruit(String title, String status, String location, String type, String applyLink, List<String> duties, List<String> requirements) {
        this.title = title;
        this.status = status;
        this.location = location;
        this.type = type;
        this.applyLink = applyLink;
        this.duties = duties != null ? duties : new ArrayList<>();
        this.requirements = requirements != null ? requirements : new ArrayList<>();
    }

    public void update(String title, String status, String location, String type, String applyLink, List<String> duties, List<String> requirements) {
        this.title = title;
        this.status = status;
        this.location = location;
        this.type = type;
        this.applyLink = applyLink;
        this.duties = duties;
        this.requirements = requirements;
    }
}