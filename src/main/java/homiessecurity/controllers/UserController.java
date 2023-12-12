package homiessecurity.controllers;

import homiessecurity.dtos.UserDto;
import homiessecurity.dtos.UserRegisterDto;
import homiessecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable Integer userId){
        UserDto user = this.userService.getUserById(userId);
        System.out.println(user);
        return new ResponseEntity<UserDto>(user, HttpStatus.OK);
    }


    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        var allUsers = this.userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
}
