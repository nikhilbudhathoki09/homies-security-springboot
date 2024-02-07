package homiessecurity.controllers;

import homiessecurity.dtos.Users.UserDto;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        ApiResponse response = this.userService.deleteUser(userId);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }
}
