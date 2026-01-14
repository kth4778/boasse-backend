package hello.boassebackend.domain.notice.service;

import hello.boassebackend.domain.notice.entity.Attachment;
import hello.boassebackend.domain.notice.entity.Notice;
import hello.boassebackend.global.common.Pagination;
import hello.boassebackend.domain.notice.dto.*;
import hello.boassebackend.domain.notice.repository.AttachmentRepository;
import hello.boassebackend.domain.notice.repository.NoticeRepository;
import hello.boassebackend.global.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("게시물 목록 조회 시작");

        // page는 0부터 시작하므로 1을 빼줍니다.
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        log.info("게시물 목록 조회 완료: totalCount={}", noticePage.getTotalElements());

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
     * 공지사항 상세 조회 (조회수 증가 O)
     */
    @Transactional
    public NoticeDetailResponse getNoticeDetailWithViewCount(Long id) {
        Notice notice = findNoticeByIdOrThrow(id);

        int oldViewCount = notice.getViewCount();
        // 조회수 증가
        notice.incrementViewCount();
        
        log.debug("조회수 증가: noticeId={}, {} -> {}", id, oldViewCount, notice.getViewCount());
        log.info("게시물 조회 성공: noticeId={}, title=\"{}\", viewCount={}", 
                 id, notice.getTitle(), notice.getViewCount());

        return NoticeDetailResponse.builder()
                .success(true)
                .data(NoticeDetailResponse.Data.from(notice))
                .build();
    }

    /**
     * 공지사항 상세 조회 (조회수 증가 X)
     * 수정, 생성 후 응답용
     */
    public NoticeDetailResponse getNoticeDetail(Long id) {
        Notice notice = findNoticeByIdOrThrow(id);
        log.debug("게시물 조회 (조회수 증가 X): noticeId={}", id);

        return NoticeDetailResponse.builder()
                .success(true)
                .data(NoticeDetailResponse.Data.from(notice))
                .build();
    }

    private Notice findNoticeByIdOrThrow(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("게시물 조회 실패: noticeId={}, error=NoticeNotFound", id);
                    return new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. id=" + id);
                });
    }

    /**
     * 공지사항 생성
     */
    @Transactional
    public Long createNotice(NoticeCreateRequest request, String author) throws IOException {
        log.info("게시물 작성 시작: author={}", author);

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author) // 인증된 사용자 정보 사용
                .build();

        // 파일 저장 및 연관관계 설정
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            log.debug("첨부파일 처리 시작: count={}", request.getFiles().size());
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    Attachment attachment = fileStore.storeFile(file);
                    if (attachment != null) {
                        notice.addAttachment(attachment);
                    }
                }
            }
            log.debug("첨부파일 처리 완료");
        }

        Notice savedNotice = noticeRepository.save(notice);
        log.info("게시물 작성 성공: noticeId={}, title=\"{}\"", savedNotice.getId(), savedNotice.getTitle());
        return savedNotice.getId();
    }

    /**
     * 공지사항 수정
     */
    @Transactional
    public Long updateNotice(Long id, NoticeUpdateRequest request) throws IOException {
        log.info("게시물 수정 시작: noticeId={}", id);

        Notice notice = findNoticeByIdOrThrow(id);

        log.debug("변경 전: title=\"{}\"", notice.getTitle());
        // 1. 기본 정보 수정
        notice.update(request.getTitle(), request.getContent());
        log.debug("변경 후: title=\"{}\"", request.getTitle());

        // 2. 파일 삭제 처리
        if (StringUtils.hasText(request.getRemoveFileIds())) {
            String[] splitIds = request.getRemoveFileIds().split(",");
            log.debug("첨부파일 삭제 요청: count={}", splitIds.length);
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

        log.info("게시물 수정 완료: noticeId={}", id);
        return notice.getId();
    }

    /**
     * 공지사항 삭제
     */
    @Transactional
    public void deleteNotice(Long id) {
        log.info("게시물 삭제 시작: noticeId={}", id);

        Notice notice = findNoticeByIdOrThrow(id);
        int attachmentCount = notice.getAttachments().size();

        // 모든 첨부파일 물리적 삭제
        if (attachmentCount > 0) {
            log.debug("첨부파일 삭제: attachmentCount={}", attachmentCount);
            for (Attachment attachment : notice.getAttachments()) {
                fileStore.deleteFile(attachment.getFilename());
            }
        }

        // DB 삭제 (Cascade 설정에 의해 Attachment 엔티티도 함께 삭제됨)
        noticeRepository.delete(notice);
        log.info("게시물 삭제 완료: noticeId={}, title=\"{}\"", id, notice.getTitle());
    }
}