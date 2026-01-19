package hello.boassebackend.domain.partner.repository;

import hello.boassebackend.domain.partner.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}
