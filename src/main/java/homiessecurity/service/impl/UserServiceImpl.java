package homiessecurity.service.impl;


import homiessecurity.dtos.UserDto;
import homiessecurity.dtos.UserRegisterDto;
import homiessecurity.entities.Role;
import homiessecurity.entities.User;
import homiessecurity.exceptions.ResourceAlreadyExistsException;
import homiessecurity.exceptions.ResourceNotFoundException;
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
        return userRepo.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User", "username", name));
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

            
        User user = User.builder().username(register.getUsername())
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

//    @Override
//    public User updateUser(Integer userId, User user) {
//        return null;
//    }


}

