package homiessecurity.dtos.Services;

import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesDto {

    private String serviceName;

    private String description;

    private double perHourRate;

    private ServiceCategory category;

    private ServiceProvider provider;





}
