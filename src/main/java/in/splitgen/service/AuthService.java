package in.splitgen.service;

import in.splitgen.entity.User;
import in.splitgen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    @Transactional
    public String sendVerificationCode(String toEmail) {
        Optional<User> userOpt = userRepository.findByEmail(toEmail);
        User user=null;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            user = new User();
            user.setEmail(toEmail);

        }
        String code = generate6DigitCode();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(60);

        user.setOtp(code);
        user.setOtpExpiration(expiresAt);
        user = userRepository.save(user); // Save and get persisted User

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Splitgen: Verify your email");
        message.setText("Your verification code is: " + code);
        System.out.println("Verification code for " + toEmail + " is: " + code + " (for testing purposes)");
     //   mailSender.send(message);
        return code;
    }

    private String generate6DigitCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
