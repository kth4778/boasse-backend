package hello.boassebackend.domain.notice.repository;

import hello.boassebackend.domain.notice.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}