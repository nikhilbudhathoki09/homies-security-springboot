package homiessecurity.repository;

import homiessecurity.entities.Status;
import homiessecurity.entities.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface  ProviderRepository extends JpaRepository<ServiceProvider, Integer> {

    Boolean existsByEmail(String email);

//    <List>ServiceProvider findByCategory(Integer category);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<ServiceProvider> findByEmail(String email);

    Optional<List<ServiceProvider>> findAllByStatus(Status status);

//    @Query(value = "SELECT p FROM ServiceProvider p WHERE p.isVerified = true", nativeQuery = true)
//    Optional<List<ServiceProvider>> findVerifiedProviders();

    @Query(value = "SELECT * FROM providers p WHERE p.is_verified = true", nativeQuery = true)
    Optional<List<ServiceProvider>> findVerifiedProviders();


}
