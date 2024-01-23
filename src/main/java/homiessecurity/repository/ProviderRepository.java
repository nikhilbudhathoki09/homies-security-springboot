package homiessecurity.repository;

import homiessecurity.entities.ProviderStatus;
import homiessecurity.entities.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  ProviderRepository extends JpaRepository<ServiceProvider, Integer> {

    Boolean existsByEmail(String email);

//    <List>ServiceProvider findByCategory(Integer category);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<ServiceProvider> findByEmail(String email);

    Optional<List<ServiceProvider>> findAllByStatus(ProviderStatus status);



}
