package homiessecurity.service.impl;


import homiessecurity.dtos.Users.UserDto;
import homiessecurity.dtos.Users.UserRegisterDto;
import homiessecurity.entities.Role;
import homiessecurity.entities.User;
import homiessecurity.exceptions.ResourceAlreadyExistsException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.RoleRepository;
import homiessecurity.repository.UserRepository;
import homiessecurity.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService, UserDetailsService{

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final ModelMapper modelMapper;



    @Autowired
    public UserServiceImpl(UserRepository userRepo,
                           RoleRepository roleRepo,
                           ModelMapper modelMapper)
                           {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;

    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User", "userId", userId ));
                    System.out.println(user);
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = this.userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return user;
    }

    @Override
    public User getUserByName(String name) {
        return userRepo.findByName(name).orElseThrow(() -> new ResourceNotFoundException("User", "username", name));
    }

    @Override
    public UserDto registerUser(UserRegisterDto register) {
        //if the email and phoneNumber already exists then should try a new one
        if(userRepo.existsByEmail(register.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists. Try a new one");
        }
        if(userRepo.existsByPhoneNumber(register.getPhoneNumber())){
            throw new ResourceAlreadyExistsException("PhoneNumber is already  in use. Try a new one ");
        }

        //searching the role from the table if not found creating a role
        Role userRole = roleRepo.findByTitle("USER")
            .orElseGet(() -> roleRepo.save(Role.builder().title("USER").description("Normal User").build()));

            
        User user = User.builder().name(register.getUsername())
                                  .phoneNumber(register.getPhoneNumber())
                                  .email(register.getEmail())
                                  .password(register.getPassword())
                                  .gender(register.getGender())
                                  .createdAt(LocalDateTime.now())
                                  .address(register.getAddress())
                                  .userRoles(Set.of(userRole))
                                  .build();    

        User createdUser = userRepo.save(user);

        return this.modelMapper.map(createdUser, UserDto.class);
    }

    @Override
    public User loginUser() {
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> allUsers = this.userRepo.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        return allUsers;
    }

    @Override
    public int verifyUser(String email) {
        return userRepo.verifyUser(email);
    }

    @Override
    public ApiResponse deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "userId", userId));
        this.userRepo.delete(user);
        return new ApiResponse("User deleted successfully", true);
    }


    public UserDto updateUser(UserDto userDto) {
        User user = this.userRepo.findByEmail(userDto.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User", "email", userDto.getEmail()));

        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setAddress(userDto.getAddress());
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = this.userRepo.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }


}

