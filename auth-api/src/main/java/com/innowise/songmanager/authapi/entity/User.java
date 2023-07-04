package com.innowise.songmanager.authapi.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Data
@Document("users")
public class User {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String username;

    private String password;

    private String role;
}
