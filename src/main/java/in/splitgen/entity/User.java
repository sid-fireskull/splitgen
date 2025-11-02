package in.splitgen.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String name;

    @Column
    private LocalDate dob;

    @Column
    private String sex;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "upi_id")
    private String upiId;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "qr_uuid", unique = true, nullable = false)
    private String qrUuid;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "otp_expiration")
    private LocalDateTime otpExpiration;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        // Ensure UUID is generated if not provided (matches SQL default)
        if (this.qrUuid == null) {
            this.qrUuid = UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        // return the stored password hash (or raw password when using no-op encoder)
        return this.otp;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
