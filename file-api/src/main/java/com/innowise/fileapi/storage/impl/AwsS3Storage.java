package com.innowise.fileapi.storage.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.fileapi.exception.StorageException;
import com.innowise.fileapi.storage.Storage;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AwsS3Storage implements Storage {

    private final S3Template s3Template;

    @Value("${file-api.storage.bucket-name}")
    private String bucketName;

    @Override
    public void upload(String filePath, MultipartFile file) {
        try {
            s3Template.upload(bucketName, filePath, file.getInputStream());
        }
        catch (Exception e) {
            throw new StorageException(
                String.format("Can't save file to s3 storage. File path: %s", filePath), e);
        }
    }

    @Override
    public Resource download(String filePath) {
        try {
            return s3Template.download(bucketName, filePath);
        }
        catch (Exception e) {
            throw new StorageException(
                String.format("Can't load file from s3 storage. File path: %s", filePath), e);
        }
    }
}
