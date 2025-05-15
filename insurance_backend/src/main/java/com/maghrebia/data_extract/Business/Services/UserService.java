package com.maghrebia.data_extract.Business.Services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.maghrebia.data_extract.DTO.LoginResponse;
import com.maghrebia.data_extract.DTO.UserCreateDTO;
import com.maghrebia.data_extract.DTO.UserDTOResponse;
import com.maghrebia.data_extract.DTO.UserUpdateDTO;

public interface UserService {

    public LoginResponse login(String email, String password);
    public void createUser(UserCreateDTO userDTO);
    public void updateUser(Long userId, UserUpdateDTO userDTO);
    public void updatePassword(Long userId, String rawPassword);
    public void deleteUser(Long userId);
    public List<UserDTOResponse> getAllUsers();
    public UserDTOResponse getUserById(Long userId, String loggedInUsername);
    void updateUserImage(Long userId, MultipartFile file) throws IOException;
}
