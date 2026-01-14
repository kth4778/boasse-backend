package hello.boassebackend.domain.notice.dto;

import hello.boassebackend.domain.notice.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {
    private Long id;
    private String filename;
    private String originalName;
    private Long size;
    private String url;

    public static AttachmentDto from(Attachment attachment) {
        return AttachmentDto.builder()
                .id(attachment.getId())
                .filename(attachment.getFilename())
                .originalName(attachment.getOriginalName())
                .size(attachment.getSize())
                .url(attachment.getUrl())
                .build();
    }
}