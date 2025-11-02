package in.splitgen.entity;

import in.splitgen.constant.ParticipantStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Joins a User to a Tour and tracks their status.
 * Corresponds to the 'tour_participant' table.
 */
@Entity
@Table(name = "tour_participant", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tour_id", "user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantStatus status;

    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        this.joinedAt = LocalDateTime.now();
    }
}

