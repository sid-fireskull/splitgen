package in.splitgen.controller;

import in.splitgen.constant.ParticipantStatus;
import in.splitgen.entity.TourParticipant;
import in.splitgen.entity.User;
import in.splitgen.exception.AuthenticationException;
import in.splitgen.service.CustomUserDetailService;
import in.splitgen.service.TourParticipantService;
import in.splitgen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/participants")
public class ParticipantController {

    @Autowired
    private UserService userService;
    @Autowired
    private TourParticipantService tourParticipantService;


    // Requires the authenticated user to be the Admin of the Tour specified by {tourId}
    @PutMapping("{tourId}/{userId}/status")
    @PreAuthorize("@permissionEvaluator.isAdmin(authentication, #tourId)")
    public ResponseEntity<?> updateParticipantStatus(
            @PathVariable Long tourId,
            @PathVariable Long userId,
            Authentication authentication,
            @RequestBody Map<String, String> statusUpdateRequest)
    {
        String status = statusUpdateRequest.get("status");
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Unauthorized", "User is not authenticated");
        }

        String email = authentication.getName();
        if (email == null || email.isEmpty()) {
            throw new AuthenticationException("Unauthorized", "Invalid authentication principal");
        }

        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new AuthenticationException("Unauthorized", "User not found");
        }

        TourParticipant tourParticipant = tourParticipantService.getTourParticipant(tourId,userId);
        if(status!=null && status.equals("APPROVED"))
            tourParticipant.setStatus(ParticipantStatus.APPROVED);
        else if(status!=null && status.equals("REJECTED"))
            tourParticipant.setStatus(ParticipantStatus.REJECTED);

        return ResponseEntity.ok("Status updated.");
    }

    // Example of another Admin-only endpoint
    @DeleteMapping("/{userId}")
    @PreAuthorize("@permissionEvaluator.isAdmin(authentication, #tourId)")
    public ResponseEntity<?> removeParticipant(
            @PathVariable Long tourId,
            @PathVariable Long userId)
    {
        // Logic to remove the participant and clean up splits
        // ...
        return ResponseEntity.noContent().build();
    }
}
