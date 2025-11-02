package in.splitgen.controller;

import in.splitgen.entity.User;
import in.splitgen.exception.AuthenticationException;
import in.splitgen.model.UserInfo;
import in.splitgen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test()
    {
        return "User Controller is working";
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Unauthorized", "User is not authenticated");
        }
        String email = authentication.getName();
        if (email == null || email.isEmpty()) {
            throw new AuthenticationException("Unauthorized", "Invalid authentication principal");
        }

        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        User user = optionalUser.get();

        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setEmail(user.getEmail());
        userInfo.setName(user.getName());
        userInfo.setSex(user.getSex());
        if (user.getDob() != null) {
            userInfo.setDob(user.getDob().format(DOB_FORMATTER));
        } else {
            userInfo.setDob(null);
        }
        userInfo.setMobileNo(user.getMobileNo());
        userInfo.setUpiId(user.getUpiId());
        userInfo.setQrUuid(user.getQrUuid());

        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserInfo> updateUserInfo(Authentication authentication, @RequestBody UserInfo updates) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Unauthorized", "User is not authenticated");
        }
        String email = authentication.getName();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            UserInfo updated = userService.updateUserInfo(updates, email);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
