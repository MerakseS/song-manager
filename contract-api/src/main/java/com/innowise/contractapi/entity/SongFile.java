package com.innowise.contractapi.entity;

import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document("songs")
public class SongFile {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String fileName;

    private String filePath;

    private StorageType storageType;

    @Transient
    @JsonIgnore
    private Resource file;
}
