package homiessecurity.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    private String description;

    private String appointmentImage;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime appointmentDate;

    private String arrivalTime;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ServiceProvider provider;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String additionalImage;

}
