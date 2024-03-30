package homiessecurity.dtos.Appointments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {

    private String description;
    private String arrivalTime;
    private LocalDate arrivalDate;
    //TODO: appointment images
    //private String appointmentImage;
    private String detailedLocation;
}
