package hello.boassebackend.domain.notice.repository;

import hello.boassebackend.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    Page<Notice> findByTitleContaining(String title, Pageable pageable);
    
    Page<Notice> findByContentContaining(String content, Pageable pageable);
    
    Page<Notice> findByAuthorContaining(String author, Pageable pageable);

    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword%")
    Page<Notice> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
}