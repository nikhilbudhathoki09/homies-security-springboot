package homiessecurity.dtos.Services;


import jakarta.annotation.Nullable;
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

    private Double perHourRate;

    private String  categoryName;

    @Nullable
    private MultipartFile serviceImage;


    
}
