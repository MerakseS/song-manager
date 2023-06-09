package com.innowise.fileapi.storage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.contractapi.exception.StorageException;
import com.innowise.fileapi.storage.Storage;

@Component
public class LocalStorage implements Storage {

    @Value("${file-api.storage.local-path}")
    private String storagePath;

    @Override
    public void upload(String filePath, MultipartFile multipartFile) {
        File file = new File(storagePath, filePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }
        catch (IOException e) {
            throw new StorageException(
                String.format("Can't save file to a local storage. File path: %s", file.getAbsolutePath()), e);
        }
    }

    @Override
    public Resource download(String filePath) {
        try {
            File file = new File(storagePath, filePath);
            return new InputStreamResource(new FileInputStream(file));
        }
        catch (IOException e) {
            throw new StorageException(
                String.format("Can't load file from a local storage. File path: %s", filePath), e);
        }
    }

    @Override
    public void delete(String filePath) {
        File file = new File(storagePath, filePath);
        if (!file.delete()) {
            throw new StorageException(String.format("Failed to delete file %s in local storage", filePath));
        }
    }
}
