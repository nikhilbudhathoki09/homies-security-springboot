package homiessecurity.dtos.Providers;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateProviderRequestDto {

    @Size(min = 3, max = 20, message = "Provider name must be between 3 and 20 characters")
    private String providerName;

    @Size(min = 10, max = 100, message = "Description must be between 10 and 100 characters")
    private String description;

    @Size(min = 10, max = 10, message = "Phone Number must be 10 characters")
    private String phoneNumber;

    @Size(min = 10, max = 100, message = "Address must be between 10 and 100 characters")
    private String address;

    @Nullable
    private MultipartFile providerImage;

    private double yearOfExperience;
    private double minServicePrice;
    private double maxServicePrice;


}

