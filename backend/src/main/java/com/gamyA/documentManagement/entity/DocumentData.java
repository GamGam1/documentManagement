package com.gamyA.documentManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documentData")
public class DocumentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @Positive(message = "Enter a valid user ID")
    private Long userId;

    @Pattern(
            regexp = "^[^/\\\\:*?\"<>|]*$",
            message = "Must not contain any of the following characters: / \\ : * ? \" < > |"
    )
    @NotNull(message = "Enter a non-empty documentName")
    private String documentName;


    @JsonFormat(pattern = "MMM d yyyy HH:mm:ss")
    private LocalDateTime uploadDate;


    private String fileSize;


    private String contentType;

    @NotNull(message = "Enter a category")
    @Pattern(
            regexp = "^[^/\\\\:*?\"<>|]*$",
            message = "Must not contain any of the following characters: / \\ : * ? \" < > |"
    )
    private String category;

    private String s3Key;

    private Boolean favorite;

    private String fileExtension;

    public DocumentData() {
    }

    @Autowired
    public DocumentData(Long userId, String documentName, LocalDateTime uploadDate, String fileSize, String contentType, String category, Boolean favorite, String s3Key, String fileExtension) {
        this.userId = userId;
        this.documentName = documentName;
        this.uploadDate = uploadDate;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.category = category;
        this.favorite = favorite;
        this.s3Key = s3Key;
        this.fileExtension = fileExtension;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
