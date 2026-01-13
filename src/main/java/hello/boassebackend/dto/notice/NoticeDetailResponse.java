package hello.boassebackend.dto.notice;

import hello.boassebackend.domain.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDetailResponse {
    private boolean success;
    private Data data;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Integer viewCount;
        private List<AttachmentDto> attachments;
        private LocalDateTime createdAt;

        public static Data from(Notice notice) {
            return Data.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .author(notice.getAuthor())
                    .viewCount(notice.getViewCount())
                    .createdAt(notice.getCreatedAt())
                    .attachments(notice.getAttachments().stream()
                            .map(AttachmentDto::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
