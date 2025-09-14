package com.maghrebia.data_extract.Web.RestControllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maghrebia.data_extract.Business.Services.UserService;
import com.maghrebia.data_extract.DTO.PasswordUpdateDTO;
import com.maghrebia.data_extract.DTO.UserCreateDTO;
import com.maghrebia.data_extract.DTO.UserDTOResponse;
import com.maghrebia.data_extract.DTO.UserUpdateDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Admins can get all users
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AGENT')")
    @GetMapping
    public List<UserDTOResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'AGENT')")
    @GetMapping("/{userId}")
    public UserDTOResponse getUserById(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserById(userId, userDetails.getUsername());
    }

    // Only Admins can create a user
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserCreateDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok("User created successfully");
    }

    // Only Admins can update a user
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateDTO userDTO) {
        userService.updateUser(userId, userDTO);
        return ResponseEntity.ok("User updated successfully");
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @RequestBody @Valid PasswordUpdateDTO dto) {
        userService.updatePassword(userId, dto.getPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping(value = "/{userId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUserImage(
            @PathVariable Long userId,
            @RequestParam("image") MultipartFile file) throws IOException {
        userService.updateUserImage(userId, file);
        return ResponseEntity.ok("Image updated successfully");
    }

    // Only Admins can delete a user
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
