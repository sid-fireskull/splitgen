package in.splitgen.config;

import in.splitgen.service.TourService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("permissionEvaluator")
public class TourPermissionEvaluator implements PermissionEvaluator {

    // You will need a TourService implementation to check admin status against the DB
    private final TourService tourService;

    public TourPermissionEvaluator(TourService tourService) {
        this.tourService = tourService;
    }

    /**
     * Primary method used by @PreAuthorize("@permissionEvaluator.isAdmin(...)")
     * when checking permissions based on a domain object (like a Tour ID).
     * * In the expression @PreAuthorize("@permissionEvaluator.isAdmin(#tourId)"):
     * - targetDomainObject is the target (tour ID).
     * - permission is the string "isAdmin".
     */
    @Override
    public boolean hasPermission(
            Authentication authentication,
            Object targetDomainObject,
            Object permission)
    {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (targetDomainObject instanceof Long && "isAdmin".equals(permission)) {
            Long tourId = (Long) targetDomainObject;
            String userEmail = authentication.getName(); // Principal (user email)

            // Delegating the actual database check to the service layer
            return tourService.isAdminOfTour(tourId, userEmail);
        }

        // Add other domain checks if needed, otherwise deny by default
        return false;
    }

    /**
     * Required by PermissionEvaluator interface, but typically not used
     * when checking against a simple domain object ID (like we do with tourId).
     */
    @Override
    public boolean hasPermission(
            Authentication authentication,
            Serializable targetId,
            String targetType,
            Object permission)
    {
        // We defer to the other hasPermission method by calling it with the ID
        return hasPermission(authentication, targetId, permission);
    }
}

