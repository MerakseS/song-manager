package com.innowise.fileapi.storage.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.contractapi.exception.StorageException;
import com.innowise.fileapi.storage.Storage;

import io.awspring.cloud.s3.S3Template;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Component
@RequiredArgsConstructor
public class AwsS3Storage implements Storage {

    private final S3Template s3Template;
    private final S3Client s3Client;

    @Value("${file-api.storage.bucket-name}")
    private String bucketName;

    @PostConstruct
    private void init() {
        if (!isBucketExists(bucketName)) {
            s3Template.createBucket(bucketName);
        }
    }

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
        return s3Template.download(bucketName, filePath);
    }

    @Override
    public void delete(String filePath) {
        s3Template.deleteObject(bucketName, filePath);
    }

    private boolean isBucketExists(String bucketName) {
        try {
            s3Client.headBucket(builder -> builder.bucket(bucketName).build());
            return true;
        }
        catch (NoSuchBucketException | BucketAlreadyOwnedByYouException e) {
            return false;
        }
    }
}
