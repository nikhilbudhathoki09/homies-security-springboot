package homiessecurity.repository;

import homiessecurity.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isVerified = TRUE WHERE a.email = ?1")
    int verifyUser(String email);
}
