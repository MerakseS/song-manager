package com.innowise.songmanager.authapi.repository.migration;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.innowise.songmanager.authapi.entity.User;
import com.innowise.songmanager.authapi.repository.UserRepository;

@ChangeLog
public class UserRepositoryChangeLog {

    @ChangeSet(order = "001", id = "initUsers", author = "Sergei Losev")
    public void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole("USER");

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setRole("ADMIN");

        userRepository.saveAll(List.of(user, admin));
    }
}
