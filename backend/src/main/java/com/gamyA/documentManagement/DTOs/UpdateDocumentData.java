package com.gamyA.documentManagement.DTOs;

public class UpdateDocumentData {

    private String documentName;

    private String category;

    private Boolean favorite;

    public UpdateDocumentData() {
    }

    public UpdateDocumentData(String documentName, String category, Boolean favorite) {
        this.documentName = documentName;
        this.category = category;
        this.favorite = favorite;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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
}
