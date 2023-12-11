package homiessecurity.service;

import homiessecurity.dtos.UserDto;
import homiessecurity.dtos.UserRegisterDto;
import homiessecurity.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserDto getUserById(Integer userId);
    User getUserByEmail(String email);
    User getUserByName(String name);
    UserDto registerUser(UserRegisterDto registerDto);
    User loginUser();
//    User updateUser(Integer userId, User user);
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
