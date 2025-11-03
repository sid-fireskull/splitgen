package in.splitgen.repository;

import in.splitgen.entity.TourParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourParticipantRepository extends JpaRepository<TourParticipant, Long> {
    public Optional<TourParticipant> findByTourIdAndUserId(Long tourId, Long userId);
}
