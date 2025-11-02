package in.splitgen.controller;


import in.splitgen.entity.User;
import in.splitgen.exception.AuthenticationException;
import in.splitgen.model.AuthVerify;
import in.splitgen.service.AuthService;
import in.splitgen.service.CustomUserDetailService;
import in.splitgen.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private CustomUserDetailService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        String code = authService.sendVerificationCode(email);
        if (code != null && !code.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "Verification code sent successfully", "email", email));
        } else {
            return ResponseEntity.status(500).body("Failed to send verification code");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthVerify> verifyCode(@RequestBody Map<String, String> request) throws Exception {
        String email = request.get("email");
        String verificationCode = request.get("otp");
        if (email == null || email.isEmpty() || verificationCode == null || verificationCode.isEmpty()) {
            throw new AuthenticationException("Email and verification code are required", null);
        }
        User user = userService.loadUserByUsername(email);

        if (user.getOtp() == null || !user.getOtp().equals(verificationCode)) {
            throw new AuthenticationException("Invalid verification code", null);
        }
        if (user.getOtpExpiration() == null || user.getOtpExpiration().isBefore(java.time.LocalDateTime.now())) {
            throw new AuthenticationException("Session Expired", null);
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, verificationCode));
        } catch (Exception ex) {
            throw new AuthenticationException("Authentication failed: " + ex.getMessage(), null);
        }
        String jwtToken = jwtUtil.generateToken(email);
        return ResponseEntity.ok(new AuthVerify(email, "Verification successful", jwtToken));
    }
}


