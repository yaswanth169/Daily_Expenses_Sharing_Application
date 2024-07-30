package com.expensesharing.com.expensesharing.service;

import com.expensesharing.com.expensesharing.entity.User;
import com.expensesharing.com.expensesharing.repositories.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new ValidationException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ValidationException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

