package homiessecurity.dtos;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String userImage;


}
