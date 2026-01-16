package hello.boassebackend.domain.recruit.service;

import hello.boassebackend.domain.recruit.dto.*;
import hello.boassebackend.domain.recruit.entity.Recruit;
import hello.boassebackend.domain.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {

    private final RecruitRepository recruitRepository;

    /**
     * 채용공고 목록 조회 (업무/요건 포함)
     */
    public RecruitListResponse getRecruits() {
        List<Recruit> recruits = recruitRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        
        List<RecruitItem> items = recruits.stream()
                .map(RecruitItem::from)
                .collect(Collectors.toList());

        return RecruitListResponse.builder()
                .success(true)
                .data(items)
                .build();
    }

    /**
     * 채용공고 상세 조회
     */
    public RecruitDetailResponse getRecruitDetail(Long id) {
        Recruit recruit = findRecruitByIdOrThrow(id);
        return RecruitDetailResponse.builder()
                .success(true)
                .data(RecruitItem.from(recruit))
                .build();
    }

    /**
     * 채용공고 작성
     */
    @Transactional
    public Long createRecruit(RecruitCreateRequest request) {
        Recruit recruit = Recruit.builder()
                .title(request.getTitle())
                .status(request.getStatus())
                .location(request.getLocation())
                .type(request.getType())
                .applyLink(request.getApplyLink())
                .duties(request.getRecruit_duties())
                .requirements(request.getRecruit_requirements())
                .build();

        Recruit savedRecruit = recruitRepository.save(recruit);
        log.info("채용공고 작성 성공: recruitId={}, title=\"¨{}\"", savedRecruit.getId(), savedRecruit.getTitle());
        return savedRecruit.getId();
    }

    /**
     * 채용공고 수정
     */
    @Transactional
    public Long updateRecruit(Long id, RecruitCreateRequest request) {
        Recruit recruit = findRecruitByIdOrThrow(id);

        recruit.update(
                request.getTitle(),
                request.getStatus(),
                request.getLocation(),
                request.getType(),
                request.getApplyLink(),
                request.getRecruit_duties(),
                request.getRecruit_requirements()
        );

        log.info("채용공고 수정 완료: recruitId={}", id);
        return recruit.getId();
    }

    /**
     * 채용공고 삭제
     */
    @Transactional
    public void deleteRecruit(Long id) {
        Recruit recruit = findRecruitByIdOrThrow(id);
        recruitRepository.delete(recruit);
        log.info("채용공고 삭제 완료: recruitId={}, title=\"¨{}\"", id, recruit.getTitle());
    }

    private Recruit findRecruitByIdOrThrow(Long id) {
        return recruitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 채용공고를 찾을 수 없습니다. id=" + id));
    }
}