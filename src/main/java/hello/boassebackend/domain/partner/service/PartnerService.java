package hello.boassebackend.domain.partner.service;

import hello.boassebackend.domain.notice.entity.Attachment;
import hello.boassebackend.domain.partner.dto.PartnerCreateRequest;
import hello.boassebackend.domain.partner.dto.PartnerItem;
import hello.boassebackend.domain.partner.dto.PartnerListResponse;
import hello.boassebackend.domain.partner.dto.PartnerUpdateRequest;
import hello.boassebackend.domain.partner.entity.Partner;
import hello.boassebackend.domain.partner.repository.PartnerRepository;
import hello.boassebackend.global.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final FileStore fileStore;

    /**
     * 파트너 목록 조회
     */
    public PartnerListResponse getPartners() {
        List<Partner> partners = partnerRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PartnerItem> items = partners.stream()
                .map(PartnerItem::from)
                .collect(Collectors.toList());

        return PartnerListResponse.builder()
                .success(true)
                .data(new PartnerListResponse.Data(items))
                .build();
    }

    /**
     * 파트너 상세 조회
     */
    public PartnerItem getPartner(Long id) {
        Partner partner = findPartnerByIdOrThrow(id);
        return PartnerItem.from(partner);
    }

    /**
     * 파트너 등록
     */
    @Transactional
    public Long createPartner(PartnerCreateRequest request) throws IOException {
        String logoUrl = "";
        
        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            Attachment attachment = fileStore.storeFile(request.getLogo());
            if (attachment != null) {
                logoUrl = attachment.getUrl();
            }
        }

        Partner partner = Partner.builder()
                .name(request.getName())
                .link(request.getLink())
                .logo(logoUrl)
                .build();

        Partner savedPartner = partnerRepository.save(partner);
        return savedPartner.getId();
    }

    /**
     * 파트너 수정
     */
    @Transactional
    public Long updatePartner(Long id, PartnerUpdateRequest request) throws IOException {
        Partner partner = findPartnerByIdOrThrow(id);

        String logoUrl = null;
        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            // 기존 파일 삭제 (선택 사항 - 파일명을 알 수 없으면 생략하거나, URL 파싱 필요)
            // 현재 Partner 엔티티는 full URL만 저장하므로 파일명 추출이 필요함.
            // FileStore 로직상 /api/v1/files/{filename} 형식이므로 파싱 가능
            deleteOldFile(partner.getLogo());

            Attachment attachment = fileStore.storeFile(request.getLogo());
            if (attachment != null) {
                logoUrl = attachment.getUrl();
            }
        }

        partner.update(request.getName(), request.getLink(), logoUrl);
        return partner.getId();
    }

    /**
     * 파트너 삭제
     */
    @Transactional
    public void deletePartner(Long id) {
        Partner partner = findPartnerByIdOrThrow(id);
        
        // 로고 파일 삭제
        deleteOldFile(partner.getLogo());
        
        partnerRepository.delete(partner);
    }

    private Partner findPartnerByIdOrThrow(Long id) {
        return partnerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파트너를 찾을 수 없습니다. id=" + id));
    }

    private void deleteOldFile(String fileUrl) {
        if (fileUrl != null && fileUrl.contains("/api/v1/files/")) {
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            fileStore.deleteFile(filename);
        }
    }
}
