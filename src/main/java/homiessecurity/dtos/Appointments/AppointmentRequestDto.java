package homiessecurity.dtos.Appointments;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {

    private String description;
    private String arrivalTime;
    private LocalDate arrivalDate;
    private String detailedLocation;
    private String appointmentImageUrl;
}
