package com.expensesharing.com.expensesharing.service;

import com.expensesharing.com.expensesharing.entity.User;
import com.expensesharing.com.expensesharing.repositories.UserRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_whenEmailDoesNotExist_shouldSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_whenEmailExists_shouldThrowValidationException() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
    }

//    @Test
//    void getUserById_whenUserExists_shouldReturnUser() {
//        Long id = 1L;
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        User foundUser = userService.getUserById(id);
//
//        assertNotNull(foundUser);
//        assertEquals(user.getId(), foundUser.getId());
//        verify(userRepository).findById(user.getId());
//    }

    @Test
    void getUserById_whenUserDoesNotExist_shouldThrowValidationException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        verify(userRepository).findAll();
    }
}
