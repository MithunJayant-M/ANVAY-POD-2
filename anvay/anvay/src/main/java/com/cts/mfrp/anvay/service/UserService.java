package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.User;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(Long userId, User user);
    void deleteUser(Long userId);
}
