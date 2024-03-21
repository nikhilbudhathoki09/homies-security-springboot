package homiessecurity.dtos.Appointments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {

    private String description;
    private String arrivalTime;
    private LocalDateTime arrivalDate;
    private Integer providerId;

    //TODO: appointment images
    //private String appointmentImage;
}
