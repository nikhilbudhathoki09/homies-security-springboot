package homiessecurity.repository;

import homiessecurity.entities.Appointment;
import homiessecurity.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

//    List<Appointment> findByAppointmentDateAndStatus(LocalDate appointmentDate, Status status);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = :date AND a.status = :status")
    List<Appointment> findByAppointmentDateAndStatus(@Param("date") LocalDate date, @Param("status") Status status);

    @Query("SELECT a FROM Appointment a WHERE a.provider.providerId = :providerId AND a.status = :status")
    Optional<List<Appointment>> findByProviderIdAndStatus(@Param("providerId") Integer providerId, @Param("status") Status status);


}
