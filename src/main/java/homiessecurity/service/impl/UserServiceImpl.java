package homiessecurity.service.impl;


import homiessecurity.dtos.Users.UpdateUserDto;
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
        return userRepo.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", name));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> allUsers = this.userRepo.findAll().stream().map(user ->
                modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
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

    @Override
    public User getRawUserById(Integer userId) {
        return this.userRepo.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "userId", userId));
    }

    @Override
    public User saveUser(User user) {
        return this.userRepo.save(user);
    }


    public UserDto updateUser(Integer userId, UpdateUserDto updateUserDto) {
        User user = getRawUserById(userId);

        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        if (updateUserDto.getPhoneNumber() != null) {
            if(userRepo.existsByPhoneNumber(updateUserDto.getPhoneNumber())){
                throw new ResourceAlreadyExistsException("PhoneNumber is already  in use. Try a new one ");
            }else{
                user.setPhoneNumber(updateUserDto.getPhoneNumber());
            }
        }
        if (updateUserDto.getAddress() != null) {
            user.setAddress(updateUserDto.getAddress());
        }
        if (updateUserDto.getGender() != null) {
            user.setGender(updateUserDto.getGender());
        }
        if (updateUserDto.getUserImageUrl() != null) {
            user.setUserImage(updateUserDto.getUserImageUrl());
        }

        User updatedUser = userRepo.save(user);

        // Assuming you have a method to convert a User entity to UserDto
        return modelMapper.map(updatedUser, UserDto.class);
    }




}

