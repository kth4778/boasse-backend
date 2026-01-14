package hello.boassebackend.domain.notice.repository;

import hello.boassebackend.domain.notice.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findByFilename(String filename);
}