package homiessecurity.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String otp;
    private String verificationToken;
    private LocalDateTime sentAt;
    private LocalDateTime verifiedAt;



    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    User user;

    @JoinColumn(name = "provider_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    ServiceProvider provider;




}
