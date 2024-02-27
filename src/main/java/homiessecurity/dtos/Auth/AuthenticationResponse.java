package homiessecurity.dtos.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import homiessecurity.dtos.Users.UserDto;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;


    private UserDto user;

    private ServiceProvider provider;



}
