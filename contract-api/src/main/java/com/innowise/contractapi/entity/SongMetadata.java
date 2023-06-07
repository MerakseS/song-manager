package com.innowise.contractapi.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Data
public class SongMetadata {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String name;

    private String link;

    private Album album;

    private List<Artist> artists;
}
