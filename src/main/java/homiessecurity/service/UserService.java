package homiessecurity.service;

import homiessecurity.dtos.Users.UserDto;
import homiessecurity.dtos.Users.UserRegisterDto;
import homiessecurity.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    UserDto getUserById(Integer userId);
    User getUserByEmail(String email);
    User getUserByName(String name);
    UserDto registerUser(UserRegisterDto registerDto);
    User loginUser();
//    User updateUser(Integer userId, User user);
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    List<UserDto> getAllUsers();

    int verifyUser(String email);


}
