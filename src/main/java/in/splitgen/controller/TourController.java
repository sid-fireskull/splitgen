package in.splitgen.controller;

import in.splitgen.entity.Tour;
import in.splitgen.entity.User;
import in.splitgen.exception.AuthenticationException;
import in.splitgen.model.TourRequest;
import in.splitgen.service.CustomUserDetailService;
import in.splitgen.service.TourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class TourController {
    private final TourService tourService;
    private final CustomUserDetailService customUserDetailService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public TourController(TourService tourService, CustomUserDetailService customUserDetailService) {
        this.tourService = tourService;
        this.customUserDetailService = customUserDetailService;
    }

    @PostMapping("/tour")
    public ResponseEntity<Tour> createTour(Authentication authentication, @Valid @RequestBody TourRequest tour) {
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Unauthorized", "User is not authenticated");
        }
        User user = customUserDetailService.loadUserByUsername(authentication.getName());

        String name = tour.getName().trim();
        String description = Objects.toString(tour.getDescription());
        String startDate = Objects.toString(tour.getStartDate());
        String endDate = Objects.toString(tour.getEndDate());

        Tour newTour = new Tour();
        newTour.setName(name);
        newTour.setDescription(description);
        newTour.setStartDate(LocalDate.parse(startDate,formatter));
        newTour.setEndDate(LocalDate.parse(endDate, formatter));
        tourService.createTour(newTour, user);
        return new ResponseEntity<>(newTour, HttpStatus.CREATED);
    }

}
