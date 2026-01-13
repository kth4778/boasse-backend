package hello.boassebackend.dto.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.boassebackend.domain.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeItem {
    private Long id;
    private String title;
    private String author;
    private Integer viewCount;
    private boolean hasAttachments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static NoticeItem from(Notice notice) {
        return NoticeItem.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .author(notice.getAuthor())
                .viewCount(notice.getViewCount())
                .hasAttachments(notice.getAttachments() != null && !notice.getAttachments().isEmpty())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
