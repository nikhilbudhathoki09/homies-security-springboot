package homiessecurity.dtos.Users;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @NotBlank(message = "This field cannot be set blank")
    private String username;

    @Email(message = "Please enter a valid email")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Size(min = 8, max = 20, message = "Password must be 8-20 characters")
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$",
            message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
    private String password;


    @Size(min = 10, max = 10, message ="Phone Number must be 10 digits")
    @NotEmpty(message = "PhoneNumber is mandatory")
    private String phoneNumber;


    @NotEmpty(message = "Address cannot be null")
    private String address;

    @NotEmpty(message = "Please select a gender")
    private String gender;

    
    private MultipartFile userImage;









}
