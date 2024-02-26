package homiessecurity.dtos.Services;

import homiessecurity.entities.ServiceCategory;
import homiessecurity.entities.ServiceProvider;
import org.springframework.web.multipart.MultipartFile;

public class ServicesDto {

    private String serviceName;

    private String description;

    private double perHourRate;

    private ServiceCategory category;

    private ServiceProvider provider;

    private MultipartFile serviceImage;

}
