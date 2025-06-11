package com.gamyA.documentManagement.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateDocumentData {
    @Pattern(
            regexp = "^[^/\\\\:*?\"<>|]*$",
            message = "Must not contain any of the following characters: / \\ : * ? \" < > |"
    )
    @NotNull(message = "Enter a non-empty documentName")
    private String documentName;

    @Pattern(
            regexp = "^[^/\\\\:*?\"<>|]*$",
            message = "Must not contain any of the following characters: / \\ : * ? \" < > |"
    )
    @NotNull(message = "Enter a non-empty category")
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
