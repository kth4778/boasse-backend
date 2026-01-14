package hello.boassebackend.domain.notice.entity;

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
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 명세서 상 author 필드가 존재하므로 추가. 실제 구현 시에는 인증 정보에서 가져오거나 관리자 ID 등을 매핑.
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Integer viewCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 1:N 관계 설정 (Notice가 연관관계의 주인은 아니지만, cascade 설정을 위해 매핑)
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @Builder
    public Notice(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.viewCount = 0; // 초기 조회수 0
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    // 연관관계 편의 메서드
    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setNotice(this);
    }
}