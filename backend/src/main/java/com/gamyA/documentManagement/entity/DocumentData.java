package com.gamyA.documentManagement.entity;

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

    //@NotNull(message = "Enter a document name")
    private String documentName;

    //@NotNull(message = "Enter a date")
    private LocalDateTime uploadDate;

    //@NotNull(message = "Enter file size")
    private String fileSize;

    //@NotNull(message = "Enter a content Type")
    private String contentType;

    private String category;

    private String s3Key;

    private Boolean favorite;

    public DocumentData() {
    }

    @Autowired
    public DocumentData(Long userId, String documentName, LocalDateTime uploadDate, String fileSize, String contentType, String category, Boolean favorite, String s3Key) {
        this.userId = userId;
        this.documentName = documentName;
        this.uploadDate = uploadDate;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.category = category;
        this.favorite = favorite;
        this.s3Key = s3Key;
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
}
