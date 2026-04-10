package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.User;

public interface UserService {
    void saveUser(User user);
    User loginUser(String email, String password);
    List<User> getAllUsers();
    void deleteUserById(Long id);
    User getUserById(Long id);
    
    // Add this line
    User findUserByEmail(String email); 
}