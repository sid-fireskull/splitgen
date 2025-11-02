package in.splitgen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AppConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configure mailSender properties as needed
        // mailSender.setHost("smtp.example.com");
        // mailSender.setPort(587);
        // mailSender.setUsername("your_email");
        // mailSender.setPassword("your_password");
        return mailSender;
    }
}

