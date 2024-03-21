package homiessecurity.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private LocalDateTime appointmentDate;

    private String arrivalTime;



    @ManyToOne
    @JoinColumn(name = "provider_id")
    @JsonBackReference
    private ServiceProvider provider;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

//    @ManyToMany(targetEntity = Services.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "appointment_services", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
//    @JsonManagedReference
//    private List<Services> allServices;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference
    private Services service;


//    private String additionalImage;

}
