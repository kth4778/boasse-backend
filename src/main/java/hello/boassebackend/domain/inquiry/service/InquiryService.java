package hello.boassebackend.domain.inquiry.service;

import hello.boassebackend.domain.inquiry.dto.InquiryCreateRequest;
import hello.boassebackend.domain.inquiry.dto.InquiryItem;
import hello.boassebackend.domain.inquiry.dto.InquiryListResponse;
import hello.boassebackend.domain.inquiry.entity.Inquiry;
import hello.boassebackend.domain.inquiry.repository.InquiryRepository;
import hello.boassebackend.global.common.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    /**
     * 1:1 문의 등록
     */
    @Transactional
    public Long createInquiry(InquiryCreateRequest request) {
        Inquiry inquiry = Inquiry.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .isAgreed(request.getIsAgreed())
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        log.info("1:1 문의 등록 성공: inquiryId={}, name={}", savedInquiry.getId(), savedInquiry.getName());
        return savedInquiry.getId();
    }

    /**
     * 문의 목록 조회 (관리자용, 페이징)
     */
    public InquiryListResponse getInquiries(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageable);

        List<InquiryItem> items = inquiryPage.getContent().stream()
                .map(InquiryItem::from)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPages(inquiryPage.getTotalPages())
                .totalCount(inquiryPage.getTotalElements())
                .limit(limit)
                .hasNext(inquiryPage.hasNext())
                .hasPrev(inquiryPage.hasPrevious())
                .build();

        return InquiryListResponse.builder()
                .success(true)
                .data(InquiryListResponse.InquiryData.builder()
                        .inquiries(items)
                        .pagination(pagination)
                        .build())
                .build();
    }

    /**
     * 문의 상세 조회 (관리자용)
     */
    public InquiryItem getInquiry(Long id) {
        Inquiry inquiry = findInquiryByIdOrThrow(id);
        return InquiryItem.from(inquiry);
    }

    /**
     * 문의 삭제 (관리자용)
     */
    @Transactional
    public void deleteInquiry(Long id) {
        Inquiry inquiry = findInquiryByIdOrThrow(id);
        inquiryRepository.delete(inquiry);
        log.info("1:1 문의 삭제 완료: inquiryId={}", id);
    }

    private Inquiry findInquiryByIdOrThrow(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의를 찾을 수 없습니다. id=" + id));
    }
}
