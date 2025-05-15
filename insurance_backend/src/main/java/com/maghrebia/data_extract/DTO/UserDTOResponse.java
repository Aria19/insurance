package com.maghrebia.data_extract.DTO;

import java.util.List;

import com.maghrebia.data_extract.DAO.Entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTOResponse {

    private Long id;

    private String username;

    private String email;

    private String role;

    private String image;

    public UserDTOResponse toDTO(User user) {
        return new UserDTOResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getImage()
        );
    }

    public List<UserDTOResponse> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .toList();
    }

}