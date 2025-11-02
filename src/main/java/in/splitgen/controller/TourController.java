package in.splitgen.controller;

import in.splitgen.entity.Tour;
import in.splitgen.entity.User;
import in.splitgen.exception.AuthenticationException;
import in.splitgen.service.CustomUserDetailService;
import in.splitgen.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TourController {
    private final TourService tourService;
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public TourController(TourService tourService, CustomUserDetailService customUserDetailService) {
        this.tourService = tourService;
        this.customUserDetailService = customUserDetailService;
    }

    @PostMapping("/api/tour")
    public ResponseEntity<Tour> createTour(Authentication authentication, @RequestBody Map<String, ?> tour) {
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Unauthorized", "User is not authenticated");
        }
        User user = customUserDetailService.loadUserByUsername(authentication.getName());

        Tour newTour = new Tour();
        newTour.setName(tour.get("name").toString().trim());

        tourService.createTour(newTour, user);
        return new ResponseEntity<>(newTour, HttpStatus.CREATED);
    }


}
