package homiessecurity.repository;

import homiessecurity.entities.Appointment;
import homiessecurity.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

//    Optional<List<Appointment>> findAllByUserId(Integer userId);
//
//    Optional<List<Appointment>> findAllByProviderId(Integer providerId);

    @Query("SELECT a FROM Appointment a WHERE a.provider.providerId = :providerId")
    Optional<List<Appointment>> findAllByProviderId(Integer providerId);

    @Query("SELECT a FROM Appointment a WHERE a.user.userId = :userId")
    Optional<List<Appointment>> findAllByUserId(Integer userId);

    Optional<List<Appointment>> findByStatus(Status status);
}
