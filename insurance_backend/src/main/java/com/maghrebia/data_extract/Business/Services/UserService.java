package com.maghrebia.data_extract.Business.Services;

import java.util.List;

import com.maghrebia.data_extract.DTO.LoginResponse;
import com.maghrebia.data_extract.DTO.UserDTORequest;
import com.maghrebia.data_extract.DTO.UserDTOResponse;

public interface UserService {

    public LoginResponse login(String email, String password);
    public void createUser(UserDTORequest userDTO);
    public void updateUser(Long userId, UserDTORequest userDTO);
    public void deleteUser(Long userId);
    public List<UserDTOResponse> getAllUsers();
    public UserDTOResponse getUserById(Long userId, String loggedInUsername);
}
