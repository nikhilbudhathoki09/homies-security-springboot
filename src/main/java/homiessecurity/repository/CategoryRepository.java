package homiessecurity.repository;



import homiessecurity.entities.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<ServiceCategory, Integer>{

    Optional<ServiceCategory> findByTitle(String title);    

    
}
