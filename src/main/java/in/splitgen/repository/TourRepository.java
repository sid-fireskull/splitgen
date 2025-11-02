package in.splitgen.repository;

import in.splitgen.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    boolean existsByTourCode(String tourCode);
}

