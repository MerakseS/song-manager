package com.innowise.songmanager.contractapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.innowise.songmanager.contractapi.dto.SongMetadataDto;
import com.innowise.songmanager.contractapi.entity.SongMetadata;

@Mapper(componentModel = "spring")
public interface SongMetadataMapper {

    SongMetadataDto mapEntityToDto(SongMetadata songMetadata);

    List<SongMetadataDto> mapEntityListToDtoList(List<SongMetadata> songMetadataList);
}
