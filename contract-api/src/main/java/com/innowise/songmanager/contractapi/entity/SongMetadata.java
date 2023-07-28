package com.innowise.songmanager.contractapi.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongMetadata {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String name;

    private String link;

    private long duration;

    @DBRef
    private Album album;

    @DBRef
    private List<Artist> artists;
}
