package com.innowise.authapi.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.authapi.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByUsername(String username);
}
