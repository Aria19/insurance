package com.maghrebia.data_extract.Mapper;

import org.springframework.stereotype.Component;

import com.maghrebia.data_extract.DAO.Entities.User;
import com.maghrebia.data_extract.DTO.UserDTOResponse;

import java.util.List;

@Component
public class UserMapper {

    public UserDTOResponse toDTO(User user) {
        return new UserDTOResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<UserDTOResponse> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .toList();
    }
}