package homiessecurity.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String transactionId; // This will be the unique ID provided by Khalti

    private String paymentMethod; // This can be hardcoded to "Khalti" if only using Khalti

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointmentId")
    private Appointment appointment;

    // Additional Khalti specific fields
    private String token;
    private String mobile;
    private String productIdentity;
    private String productName;


}

enum PaymentStatus {
    INITIATED,
    SUCCESSFUL,
    FAILED,
    PENDING
}
