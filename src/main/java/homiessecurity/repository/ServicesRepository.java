package homiessecurity.repository;

import homiessecurity.entities.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Integer>{

    Optional<Services> findByServiceName(String serviceName);
    
    
}
