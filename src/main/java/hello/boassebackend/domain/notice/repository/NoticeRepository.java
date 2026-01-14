package hello.boassebackend.domain.notice.repository;

import hello.boassebackend.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 기본적으로 PagingAndSortingRepository의 기능을 모두 포함하고 있습니다.
}