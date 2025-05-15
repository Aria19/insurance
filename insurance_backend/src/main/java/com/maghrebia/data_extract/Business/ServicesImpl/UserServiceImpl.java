package com.maghrebia.data_extract.Business.ServicesImpl;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.maghrebia.data_extract.Business.Services.JwtService;
import com.maghrebia.data_extract.Business.Services.UserService;
import com.maghrebia.data_extract.DAO.Entities.User;
import com.maghrebia.data_extract.DAO.Repositories.UserRepository;
import com.maghrebia.data_extract.DTO.LoginResponse;
import com.maghrebia.data_extract.DTO.UserCreateDTO;
import com.maghrebia.data_extract.DTO.UserDTOResponse;
import com.maghrebia.data_extract.DTO.UserUpdateDTO;
import com.maghrebia.data_extract.Mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    // 🔹 Authenticate and generate JWT token
    public LoginResponse login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword())) {
            User user = userOptional.get();
            String token = jwtService.generateToken(email, user.getRole(), user.getUsername()); // Generate token with
                                                                                                // role

            return new LoginResponse(token, user.getRole(), user.getUsername()); // Return token & role
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // 🔹 Create a new user (only accessible by admins)
    public void createUser(UserCreateDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "e-mail est déjà utilisé");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode password
        user.setRole(userDTO.getRole()); // Default role
        user.setImage("/images/user-profile-icon.jpg");

        userRepository.save(user);

    }

    // 🔹 Update an existing user (only accessible by admins)
    public void updateUser(Long userId, UserUpdateDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
    }

    public void updatePassword(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    public void updateUserImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = saveFile(file);
        user.setImage(fileName);

        userRepository.save(user);
    }

    public String saveFile(MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path (you can also store full if needed)
        return "/uploads/" + fileName;
    }

    // 🔹 Delete a user (only accessible by admins)
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    // 🔹 Get all users (only accessible by admins)
    public List<UserDTOResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    // 🔹 Get user by ID (accessible by both admins and agents, but agents can only
    // view their own profile)
    public UserDTOResponse getUserById(Long userId, String loggedInUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (loggedInUsername.equals("ADMIN") || loggedInUsername.equals(user.getUsername())) {
            return userMapper.toDTO(user); // Convert User entity to DTO before returning
        } else {
            throw new RuntimeException("Permission denied");
        }
    }

}
