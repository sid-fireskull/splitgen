package in.splitgen.service;

import in.splitgen.entity.TourParticipant;
import in.splitgen.repository.TourParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourParticipantService {

    @Autowired
    private TourParticipantRepository tourParticipantRepository;

    public TourParticipant getTourParticipant(Long tourId, Long userId) {
        return tourParticipantRepository.findByTourIdAndUserId(tourId, userId).orElseThrow(() -> new RuntimeException("TourParticipant not found"));
    }
}
