package homiessecurity.dtos.Users;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;

    @Size(min = 10, max = 10, message = "Phone number must be 10 characters")
    private String phoneNumber;

    @Size(min = 10, max = 100, message = "Address must be between 10 and 100 characters")
    private String address;

    private String gender;

    private String userImageUrl;
}
