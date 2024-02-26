package homiessecurity.repository;

import homiessecurity.entities.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Integer>{

    Optional<Services> findByServiceName(String serviceName);

    Optional<List<Services>> findByCategoryTitle(String categoryName);



//    Optional<List<Services>> findByProviderId(Integer providerId);

    @Query("SELECT s FROM Services s WHERE s.provider.providerId = ?1")
    Optional<List<Services>> findByProviderId(Integer providerId);

    Optional<List<Services>> findByServiceNameContainingIgnoreCase(String search);

    
    
}
