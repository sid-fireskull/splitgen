package in.splitgen.service;

import in.splitgen.entity.Tour;
import in.splitgen.entity.User;
import in.splitgen.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class TourService {
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Transactional
    public Tour createTour(Tour tour, User admin) {
        tour.setTourCode(generateUniqueTourCode());
        tour.setAdmin(admin);
        return tourRepository.save(tour);
    }

    private String generateUniqueTourCode() {
        String code;
        do {
            code = randomAlphaNumeric(7);
        } while (tourRepository.existsByTourCode(code));
        return code;
    }

    private String randomAlphaNumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    @Transactional(readOnly = true)
    public Tour findById(Long id) {
        Optional<Tour> opt = tourRepository.findById(id);
        return opt.orElse(null);
    }

    @Transactional
    public Tour updateTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public boolean isAdminOfTour(Long tourId, String currentUserName) {
        Optional<Tour> optTour = tourRepository.findById(tourId);
        if (optTour.isEmpty()) {
            return false;
        }
        Tour tour = optTour.get();
        return tour.getAdmin().getEmail().equals(currentUserName);
    }
}
