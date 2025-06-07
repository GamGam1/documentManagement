package com.gamyA.documentManagement.DTOs;

public class UploadDocumentData {

    private String category;

    private Boolean favorite;

    private Long userId;

    public UploadDocumentData() {
    }

    public UploadDocumentData(Long userId, String category, Boolean favorite) {
        this.category = category;
        this.favorite = favorite;
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
