package com.gamyA.documentManagement.service;

import com.gamyA.documentManagement.repository.DocumentDataRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class DocumentService {


    @Value("${app.aws.s3.bucket-name}")
    private String bucketName;

    private S3Client s3Client;

    private DocumentDataRepo repo;




}
