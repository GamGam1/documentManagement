package com.gamyA.documentManagement.service;

import com.gamyA.documentManagement.DTOs.UpdateDocumentData;
import com.gamyA.documentManagement.entity.DocumentData;
import com.gamyA.documentManagement.repository.DocumentDataRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {


    @Value("${app.aws.s3.bucket-name}")
    private static String bucketName;

    private static S3Client s3Client;

    private static DocumentDataRepo documentDataRepo;

    @Autowired
    public DocumentService(S3Client s3Client, DocumentDataRepo documentDataRepo) {
        DocumentService.s3Client = s3Client;
        DocumentService.documentDataRepo = documentDataRepo;
    }

    /*

    Methods

    * */

    public String getPresignUrl(Long userId,Long documentId){

        DocumentData currDoc = documentDataRepo.findByDocumentIdAndUserId(documentId, userId).orElseThrow(() -> new RuntimeException("document not found by user"));
        String currS3Key = currDoc.getS3Key();

        try(S3Presigner presigner = S3Presigner.builder().region(Region.US_WEST_1).build()){
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName).key(currS3Key)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)).getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(getObjectPresignRequest).url().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get pre-signed URL");
        }

    }


    public String getShareableLink(Long userId, Long documentId, Duration duration){

        DocumentData currDoc = documentDataRepo.findByDocumentIdAndUserId(documentId, userId).orElseThrow(() -> new RuntimeException("document not found by user"));
        String currS3Key = currDoc.getS3Key();

        try(S3Presigner presigner = S3Presigner.builder().region(Region.US_WEST_1).build()){
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName).key(currS3Key)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(duration).getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(getObjectPresignRequest).url().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get pre-signed URL");
        }

    }

    public List<DocumentData> getAllDocumentData(Long userId){
        List<DocumentData> currDocs = documentDataRepo.findByUserId(userId);
        if(currDocs.isEmpty()){
            throw new RuntimeException("user not found");
        }
        return currDocs;
    }

    public List<DocumentData> getAllDocumentDataFilter(Long userId, List<String> categories, List<String> contentTypes
                                                        , LocalDateTime maxDate, LocalDateTime minDate, Boolean favorite){
        List<DocumentData> currDocs = documentDataRepo.findByUserIdFilter(userId,categories,contentTypes,minDate,maxDate,favorite);
        if(currDocs.isEmpty()){
            throw new RuntimeException("no files found");
        }
        return currDocs;
    }

    public void updateDocumentData(Long userId, Long documentId, UpdateDocumentData newDocumentData){

        DocumentData currDoc = documentDataRepo.findByDocumentIdAndUserId(documentId, userId).orElseThrow(() -> new RuntimeException("document not found by user"));

        if(newDocumentData.getDocumentName() != null){
            currDoc.setDocumentName(newDocumentData.getDocumentName());
        }

        if(newDocumentData.getCategory() != null){
            currDoc.setCategory(newDocumentData.getCategory());
        }

        if (newDocumentData.getFavorite() != null){
            currDoc.setFavorite(newDocumentData.getFavorite());
        }

        documentDataRepo.save(currDoc);

    }






}
