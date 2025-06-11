package com.gamyA.documentManagement.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UploadDocumentData {

    @NotNull(message = "Enter a category")
    @Pattern(
            regexp = "^[^/\\\\:*?\"<>|]*$",
            message = "Must not contain any of the following characters: / \\ : * ? \" < > |"
    )
    private String category;

    private Boolean favorite;


    public UploadDocumentData() {
    }

    public UploadDocumentData(Long userId, String category, Boolean favorite) {
        this.category = category;
        if(favorite == null){this.favorite = false;}
        else{this.favorite = favorite;}

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
