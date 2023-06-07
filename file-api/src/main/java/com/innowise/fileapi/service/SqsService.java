package com.innowise.fileapi.service;

import com.innowise.contractapi.dto.SongTagsDto;

public interface SqsService {

    void sendNewSong(SongTagsDto songTagsDto);
}
