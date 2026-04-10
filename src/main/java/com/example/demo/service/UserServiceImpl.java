package com.example.demo.service;

import java.util.List; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        // Matches the findByEmailAndPassword in the repository
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(); 
    }
    
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Override
    public User findUserByEmail(String email) {
        // This links your WebController call to the UserRepository query
        return userRepository.findByEmail(email);
    }
    
}