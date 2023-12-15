package homiessecurity.dtos;

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

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String userImage;
    private Set<Role> userRoles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;






}
