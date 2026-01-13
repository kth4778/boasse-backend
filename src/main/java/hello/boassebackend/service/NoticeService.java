package hello.boassebackend.service;

import hello.boassebackend.domain.entity.Attachment;
import hello.boassebackend.domain.entity.Notice;
import hello.boassebackend.dto.common.Pagination;
import hello.boassebackend.dto.notice.*;
import hello.boassebackend.repository.AttachmentRepository;
import hello.boassebackend.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AttachmentRepository attachmentRepository;
    private final FileStore fileStore;

    /**
     * 공지사항 목록 조회
     */
    public NoticeListResponse getNotices(int page, int limit) {
        // page는 0부터 시작하므로 1을 빼줍니다.
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        List<NoticeItem> noticeItems = noticePage.getContent().stream()
                .map(NoticeItem::from)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPages(noticePage.getTotalPages())
                .totalCount(noticePage.getTotalElements())
                .limit(limit)
                .hasNext(noticePage.hasNext())
                .hasPrev(noticePage.hasPrevious())
                .build();

        return NoticeListResponse.builder()
                .success(true)
                .data(NoticeListResponse.Data.builder()
                        .notices(noticeItems)
                        .pagination(pagination)
                        .build())
                .build();
    }

    /**
     * 공지사항 상세 조회
     */
    @Transactional
    public NoticeDetailResponse getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. id=" + id));

        // 조회수 증가
        notice.incrementViewCount();

        return NoticeDetailResponse.builder()
                .success(true)
                .data(NoticeDetailResponse.Data.from(notice))
                .build();
    }

    /**
     * 공지사항 생성
     */
    @Transactional
    public Long createNotice(NoticeCreateRequest request, String author) throws IOException {
        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author) // 인증된 사용자 정보 사용
                .build();

        // 파일 저장 및 연관관계 설정
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    Attachment attachment = fileStore.storeFile(file);
                    if (attachment != null) {
                        notice.addAttachment(attachment);
                    }
                }
            }
        }

        Notice savedNotice = noticeRepository.save(notice);
        return savedNotice.getId();
    }

    /**
     * 공지사항 수정
     */
    @Transactional
    public Long updateNotice(Long id, NoticeUpdateRequest request) throws IOException {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. id=" + id));

        // 1. 기본 정보 수정
        notice.update(request.getTitle(), request.getContent());

        // 2. 파일 삭제 처리
        if (StringUtils.hasText(request.getRemoveFileIds())) {
            String[] splitIds = request.getRemoveFileIds().split(",");
            for (String fileIdStr : splitIds) {
                try {
                    Long fileId = Long.parseLong(fileIdStr.trim());
                    Attachment attachment = attachmentRepository.findById(fileId)
                            .orElse(null);
                    
                    if (attachment != null && attachment.getNotice().getId().equals(id)) {
                        // 물리 파일 삭제
                        fileStore.deleteFile(attachment.getFilename());
                        // DB 삭제 (orphanRemoval=true 이므로 리스트에서 제거하면 삭제됨)
                        notice.getAttachments().remove(attachment);
                        attachment.setNotice(null); // 관계 끊기
                        // 명시적 삭제가 필요하다면 repo 호출
                        attachmentRepository.delete(attachment); 
                    }
                } catch (NumberFormatException e) {
                    // 잘못된 ID 형식 무시
                }
            }
        }

        // 3. 새 파일 추가
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    Attachment attachment = fileStore.storeFile(file);
                    if (attachment != null) {
                        notice.addAttachment(attachment);
                    }
                }
            }
        }

        return notice.getId();
    }

    /**
     * 공지사항 삭제
     */
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. id=" + id));

        // 모든 첨부파일 물리적 삭제
        for (Attachment attachment : notice.getAttachments()) {
            fileStore.deleteFile(attachment.getFilename());
        }

        // DB 삭제 (Cascade 설정에 의해 Attachment 엔티티도 함께 삭제됨)
        noticeRepository.delete(notice);
    }
}
