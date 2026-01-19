package hello.boassebackend.domain.partner.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "partner")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String link; // 파트너사 웹사이트 URL

    @Column(nullable = false, length = 500)
    private String logo; // 로고 이미지 URL (또는 경로)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Partner(String name, String link, String logo) {
        this.name = name;
        this.link = link;
        this.logo = logo;
    }

    public void update(String name, String link, String logo) {
        this.name = name;
        this.link = link;
        if (logo != null && !logo.isEmpty()) {
            this.logo = logo;
        }
    }
}
