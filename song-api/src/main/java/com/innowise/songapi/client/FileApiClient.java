package com.innowise.songapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "file-api", path = "/files")
public interface FileApiClient {

    @DeleteMapping("/{id}")
    void deleteSongFile(@PathVariable("id") String id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
