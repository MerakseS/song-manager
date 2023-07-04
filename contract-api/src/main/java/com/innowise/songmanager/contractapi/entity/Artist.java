package com.innowise.songmanager.contractapi.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Data
@Document("artists")
public class Artist {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String name;

    private String link;
}
