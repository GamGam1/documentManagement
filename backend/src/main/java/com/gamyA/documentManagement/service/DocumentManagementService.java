package com.gamyA.documentManagement.service;

import com.gamyA.documentManagement.DTOs.UpdateDocumentData;
import com.gamyA.documentManagement.DTOs.UploadDocumentData;
import com.gamyA.documentManagement.entity.DocumentData;
import com.gamyA.documentManagement.repository.DocumentManagementRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentManagementService {


    @Value("${app.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    private static S3Client s3Client;

    private static DocumentManagementRepo documentManagementRepo;

    @Autowired
    public DocumentManagementService(S3Client s3Client, DocumentManagementRepo documentManagementRepo) {
        DocumentManagementService.s3Client = s3Client;
        DocumentManagementService.documentManagementRepo = documentManagementRepo;
    }

    /*

    Methods

    * */

    public String getPresignUrl(Long userId,Long documentId){

        DocumentData currDoc = documentManagementRepo.findByDocumentIdAndUserId(documentId, userId).orElseThrow(() -> new RuntimeException("document not found by user"));
        String currS3Key = currDoc.getS3Key();

        try(S3Presigner presigner = S3Presigner.builder().region(Region.US_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey)))
                .build()){
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName).key(currS3Key)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)).getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(getObjectPresignRequest).url().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get pre-signed URL with Exception:" + e);
        }

    }


    public String getShareableLink(Long userId, Long documentId, Duration duration){
        DocumentData currDoc = documentManagementRepo.findByDocumentIdAndUserId(documentId, userId).orElseThrow(() -> new RuntimeException("document not found by user"));
        String currS3Key = currDoc.getS3Key();

        try(S3Presigner presigner = S3Presigner.builder().region(Region.US_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey)))
                .build()){
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
        List<DocumentData> currDocs = documentManagementRepo.findByUserId(userId);
        if(currDocs.isEmpty()){
            throw new RuntimeException("user not found");
        }
        return currDocs;
    }

    public List<DocumentData> getAllDocumentDataFilter(Long userId, List<String> categories, List<String> fileExtensions
                                                        , LocalDateTime maxDate, LocalDateTime minDate, Boolean favorite, String name){
        List<DocumentData> currDocs = documentManagementRepo.findByUserIdFilter(userId,categories,fileExtensions,minDate,maxDate,favorite, name);
        if(currDocs.isEmpty()){
            throw new RuntimeException("no files found");
        }
        return currDocs;
    }

    public void updateDocumentData(Long userId, Long documentId, UpdateDocumentData newDocumentData){

        DocumentData currDoc = documentManagementRepo.findByDocumentIdAndUserId(documentId, userId).orElseThrow(() -> new RuntimeException("document not found"));

        if(newDocumentData.getDocumentName() != null){
            currDoc.setDocumentName(newDocumentData.getDocumentName());
        }

        if(newDocumentData.getCategory() != null){
            currDoc.setCategory(newDocumentData.getCategory());
        }

        if (newDocumentData.getFavorite() != null){
            currDoc.setFavorite(newDocumentData.getFavorite());
        }

        documentManagementRepo.save(currDoc);

    }

    public void uploadFile(Long userId, UploadDocumentData uploadDocumentData, MultipartFile file) throws IOException {
        //get metadata: document name, upload date, get file size, content type
        Double documentSize;
        String documentSizeString;
        String fullDocumentName = file.getOriginalFilename();
        String documentName = fullDocumentName.substring(0, fullDocumentName.lastIndexOf("."));
        String fileExtension = fullDocumentName.substring(fullDocumentName.lastIndexOf(".") + 1);
        LocalDateTime uploadDate = LocalDateTime.now();
        String documentContentType = file.getContentType();

        if(file.getSize() >= (1024 * 1024)){
            documentSize = (file.getSize() / (1024.0 * 1024.0));
            documentSize = new BigDecimal(documentSize).setScale(2, RoundingMode.HALF_UP).doubleValue();
            documentSizeString = documentSize + " MB";

        }
        else{
            documentSize = (file.getSize() / 1024.0 );
            documentSize = new BigDecimal(documentSize).setScale(2, RoundingMode.HALF_UP).doubleValue();
            documentSizeString = documentSize + " KB";

        }

        //create documentData, need category, favorite, and create the s3Key
        UUID uuid =  UUID.randomUUID();
        String s3Key = userId + "/" + uuid + "." + documentContentType;
        DocumentData newDocumentData = new DocumentData(userId,
                documentName, uploadDate, documentSizeString,documentContentType,
                uploadDocumentData.getCategory(), uploadDocumentData.getFavorite(),s3Key,fileExtension);

        //upload to awss3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName).key(s3Key).contentType(file.getContentType()).build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        documentManagementRepo.save(newDocumentData);

    }

    @Transactional
    public void deleteFile(Long userId, Long documentId){
        // find from database
        DocumentData currDocumentData = documentManagementRepo.findByDocumentIdAndUserId(documentId,userId)
                .orElseThrow(() -> new RuntimeException("document is not found"));
        //delete from aws bucket and database
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName).key(currDocumentData.getS3Key()).build();
        s3Client.deleteObject(deleteObjectRequest);

        documentManagementRepo.delete(currDocumentData);

    }




}
