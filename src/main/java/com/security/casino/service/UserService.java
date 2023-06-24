package com.security.casino.service;



import com.security.casino.dto.UserDto;
import com.security.casino.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
