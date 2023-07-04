package com.innowise.songmanager.fileapi.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface Storage {

    void upload(String filePath, MultipartFile file);

    Resource download(String filePath);

    void delete(String filePath);
}
