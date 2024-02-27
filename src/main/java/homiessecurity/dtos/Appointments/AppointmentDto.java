package homiessecurity.dtos.Appointments;


import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    Integer appointmentId;

    private Integer description;

    private String appointmentDate;

    private String arrivalTime;

    private ServiceProvider provider;

    private User user;

}
