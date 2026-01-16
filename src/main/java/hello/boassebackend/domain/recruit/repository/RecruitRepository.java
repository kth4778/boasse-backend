package hello.boassebackend.domain.recruit.repository;

import hello.boassebackend.domain.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {
}