package homiessecurity.repository;

import homiessecurity.entities.Appointment;
import homiessecurity.entities.Rating;
import homiessecurity.entities.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query("SELECT r FROM Rating r WHERE r.provider.providerId = :providerId")
    Optional<List<Rating>> findAllByProviderId(Integer providerId);

    @Query("SELECT r FROM Rating r WHERE r.user.userId = :userId")
    Optional<List<Rating>> findAllByUserId(Integer userId);

    List<Rating> findAllByProvider(ServiceProvider provider);



}
