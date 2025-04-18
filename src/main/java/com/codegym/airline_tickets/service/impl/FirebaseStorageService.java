package com.codegym.airline_tickets.service.impl;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile file, String userId) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = "users/" + userId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());

        blob.createAcl(com.google.cloud.storage.Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(), com.google.cloud.storage.Acl.Role.READER));

        return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), fileName);
    }

    public void deleteFileFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;

        String bucketName = StorageClient.getInstance().bucket().getName();
        String prefix = String.format("https://storage.googleapis.com/%s/", bucketName);

        if (fileUrl.startsWith(prefix)) {
            String objectName = fileUrl.substring(prefix.length());
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(objectName);
            if (blob != null && blob.exists()) {
                blob.delete();
            }
        }
    }


}