package homiessecurity.dtos.Users;

import homiessecurity.entities.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String userImage;
    private Set<Role> userRoles;
    private boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;






}
