package homiessecurity.dtos.Services;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddServiceDto {

    private String serviceName;

    private String description;

    private double perHourRate;

    private String  categoryName;

//    private Integer providerId;
    private MultipartFile serviceImage;


    
}
