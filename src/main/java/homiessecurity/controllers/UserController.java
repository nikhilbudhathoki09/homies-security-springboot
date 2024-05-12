package homiessecurity.controllers;

import homiessecurity.dtos.Users.UpdateUserDto;
import homiessecurity.dtos.Users.UserDto;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.CloudinaryService;
import homiessecurity.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    public UserController(UserService userService, CloudinaryService cloudinaryService){
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable Integer userId){
        UserDto user = this.userService.getUserById(userId);
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

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId,
                                        @ModelAttribute UpdateUserDto updateUserDto,
                                        @RequestParam(value = "userImage", required = false) MultipartFile userImage) {
        if (userImage != null) {
            String imageUrl = cloudinaryService.uploadImage(userImage, "UserImages");
            updateUserDto.setUserImageUrl(imageUrl);
        }

        UserDto updatedUserDto = userService.updateUser(userId, updateUserDto);
        return ResponseEntity.ok(updatedUserDto);
    }


}
