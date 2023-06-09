package com.innowise.songapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.innowise.contractapi.dto.SongMetadataDto;
import com.innowise.contractapi.entity.SongMetadata;

@Mapper(componentModel = "spring")
public interface SongMetadataMapper {

    SongMetadata mapDtoToEntity(SongMetadataDto songMetadataDto);

    SongMetadataDto mapEntityToDto(SongMetadata songMetadata);

    List<SongMetadataDto> mapEntityListToDtoList(List<SongMetadata> songMetadataList);
}
