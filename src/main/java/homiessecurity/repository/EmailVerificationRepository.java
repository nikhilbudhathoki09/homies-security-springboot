package homiessecurity.repository;

import homiessecurity.entities.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Integer> {


    Optional<EmailVerification> findByVerificationToken(String token);
}
