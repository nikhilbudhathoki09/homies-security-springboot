package homiessecurity.dtos.Providers;


import homiessecurity.entities.Status;
import homiessecurity.entities.ServiceCategory;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class ProviderRegistrationRequestDto {

    @NotBlank(message = "Username cannot be set empty. ")
    @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
    private String providerName;

    @NotBlank(message = "Email cannot be set empty.")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Phone Number cannot be set empty.")
    @Size(min = 10, max = 10, message = "Phone Number must be 10 characters")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be set empty.")
    @Size(min = 10, max = 100, message = "Address must be 10-100 characters")
    private String address;


    @NotBlank(message = "Password cannot be set empty.")
    @Size(min = 8, max = 16, message = "Password must be 8-16 characters")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$",
             message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
    private String password;

    @NotBlank(message = "Description cannot be set empty.")
    @Size(min = 10, max = 100, message = "Description must be 10-100 characters")
    private String description;

    @Nullable
    private MultipartFile providerImage;

    @NotBlank(message = "Registration Document cannot be set empty.")
    private MultipartFile registrationDocument;

    @NotBlank(message = "Experience Document cannot be set empty.")
    private MultipartFile experienceDocument;



    
}
