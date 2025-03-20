package com.maghrebia.data_extract.Business.ServicesImpl;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maghrebia.data_extract.Business.Services.JwtService;
import com.maghrebia.data_extract.Business.Services.UserService;
import com.maghrebia.data_extract.DAO.Entities.User;
import com.maghrebia.data_extract.DAO.Repositories.UserRepository;
import com.maghrebia.data_extract.DTO.LoginResponse;
import com.maghrebia.data_extract.DTO.UserDTORequest;
import com.maghrebia.data_extract.DTO.UserDTOResponse;
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
        String token = jwtService.generateToken(email, user.getRole()); // Generate token with role

        return new LoginResponse(token, user.getRole()); // Return token & role
    } else {
        throw new RuntimeException("Invalid credentials");
    }
}


    // 🔹 Create a new user (only accessible by admins)
    public void createUser(UserDTORequest userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode password
        user.setRole("AGENT"); // Default role

        userRepository.save(user);

    }

    // 🔹 Update an existing user (only accessible by admins)
    public void updateUser(Long userId, UserDTORequest userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);
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
