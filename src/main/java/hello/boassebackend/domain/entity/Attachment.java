package hello.boassebackend.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename; // 저장된 파일명 (UUID 등 포함)

    @Column(nullable = false)
    private String originalName; // 원본 파일명

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String url; // 다운로드 URL 혹은 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Builder
    public Attachment(String filename, String originalName, Long size, String url) {
        this.filename = filename;
        this.originalName = originalName;
        this.size = size;
        this.url = url;
    }

    // 연관관계 설정을 위한 Setter (Notice 쪽에서 호출)
    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
