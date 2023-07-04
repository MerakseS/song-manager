package com.innowise.songmanager.fileapi.service;

import com.innowise.songmanager.contractapi.dto.SongTagsDto;

public interface SqsService {

    void sendNewSong(SongTagsDto songTagsDto);
}
