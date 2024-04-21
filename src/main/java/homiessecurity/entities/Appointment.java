package homiessecurity.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDate appointmentDate;

    private String arrivalTime;

    private String detailedLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id",referencedColumnName = "id")
    @JsonBackReference(value = "provider-appointment")
    ServiceProvider provider;


    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "user-appointment")
    User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @JsonBackReference(value = "service-appointment")
    private Services service;

}
