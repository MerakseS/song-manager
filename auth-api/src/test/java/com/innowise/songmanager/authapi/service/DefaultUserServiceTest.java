package com.innowise.songmanager.authapi.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

import com.innowise.songmanager.authapi.entity.User;
import com.innowise.songmanager.authapi.repository.UserRepository;

class DefaultUserServiceTest {

    private UserRepository userRepository;
    private UserDetailsService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new DefaultUserService(userRepository);
    }

    @Test
    void loadUserByUsername() {
        String username = "username";

        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole("USER");

        Mockito.when(userRepository.findUserByUsername(username))
            .thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);
        Assertions.assertEquals(username, userDetails.getUsername());
    }

    @Test
    void userNotExists() {
        String username = "username";

        Mockito.when(userRepository.findUserByUsername(username))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class,
            () -> userService.loadUserByUsername(username));
    }
}