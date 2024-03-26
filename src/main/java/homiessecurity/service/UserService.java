package homiessecurity.service;

import homiessecurity.dtos.Users.UpdateUserDto;
import homiessecurity.dtos.Users.UserDto;
import homiessecurity.dtos.Users.UserRegisterDto;
import homiessecurity.entities.User;
import homiessecurity.payload.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    UserDto getUserById(Integer userId);
    User getUserByEmail(String email);
    User getUserByName(String name);

    public UserDto updateUser(Integer userId, UpdateUserDto updateUserDto);
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    List<UserDto> getAllUsers();

    int verifyUser(String email);

    ApiResponse deleteUser(Integer userId);

    User getRawUserById(Integer userId);

    User saveUser(User user);


}
