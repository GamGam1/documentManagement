package com.gamyA.documentManagement.controller;

import com.gamyA.documentManagement.DTOs.UpdateDocumentData;
import com.gamyA.documentManagement.DTOs.UploadDocumentData;
import com.gamyA.documentManagement.entity.DocumentData;
import com.gamyA.documentManagement.service.DocumentManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DocumentDataController {

    private static DocumentManagementService documentManagementService;

    @Autowired
    public DocumentDataController(DocumentManagementService documentManagementService) {
        DocumentDataController.documentManagementService = documentManagementService;
    }


    /*

    GET REQUESTS

     */
    @GetMapping(value = "/api/{userId}/document")
    public List<DocumentData> getUserDocumentData(@PathVariable Long userId){
        return documentManagementService.getAllDocumentData(userId);
    }

    @GetMapping(value="/api/{userId}/document/{documentId}/download")
    public String downloadDataViaUrl(@PathVariable Long userId, @PathVariable Long documentId){
        return documentManagementService.getPresignUrl(userId,documentId);
    }

    @GetMapping(value="/api/{userId}/document/{documentId}/share")
    public String getSharableLink(@PathVariable Long userId, @PathVariable Long documentId, @RequestParam long duration){
        return documentManagementService.getShareableLink(userId, documentId, Duration.ofMinutes(duration));
    }

    @GetMapping(value = "/api/{userId}/document/filter")
    public List<DocumentData> getAllDocumentDataFilter(@PathVariable Long userId,
                                                       @RequestParam(required = false) List<String> categories,
                                                       @RequestParam(required = false) List<String> fileExtensions,
                                                       @RequestParam(required = false) LocalDateTime maxDate,
                                                       @RequestParam(required = false) LocalDateTime minDate,
                                                       @RequestParam(required = false) Boolean favorite,
                                                       @RequestParam(required = false) String documentName){
        if(maxDate == null){
            maxDate = LocalDateTime.MAX;
        }
        if(minDate == null){
            minDate = LocalDateTime.MIN;
        }
        return documentManagementService.getAllDocumentDataFilter(userId, categories, fileExtensions, maxDate, minDate, favorite, documentName);
    }

      /*

    PUT REQUESTS

     */

    @PutMapping(value="/api/{userId}/{documentId}/update")
    public void updateDocumentData(@PathVariable Long userId,
                                   @PathVariable Long documentId,
                                   @Valid @RequestBody UpdateDocumentData newDocumentData){
        documentManagementService.updateDocumentData(userId, documentId, newDocumentData);
    }

     /*

    POST REQUESTS

     */

    @PostMapping(value="/api/{userId}/upload")
    public void uploadDocument(@PathVariable Long userId,
                               @Valid @RequestBody UploadDocumentData uploadDocumentData,
                               @RequestParam MultipartFile file) throws IOException {
        documentManagementService.uploadFile(userId, uploadDocumentData, file);
    }
     /*

    Delete REQUESTS

     */
    @DeleteMapping(value = "/api/{userId}/document/{documentId}/delete")
    public void deleteDocument(@PathVariable Long userId, @PathVariable Long documentId){
        documentManagementService.deleteFile(userId, documentId);
    }
}
