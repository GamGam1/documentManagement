package com.gamyA.documentManagement.controller;

import com.gamyA.documentManagement.DTOs.UpdateDocumentData;
import com.gamyA.documentManagement.DTOs.UploadDocumentData;
import com.gamyA.documentManagement.entity.DocumentData;
import com.gamyA.documentManagement.service.DocumentManagementService;
import com.gamyA.documentManagement.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/documents")
public class DocumentDataController {

    private DocumentManagementService documentManagementService;
    private JWTService jwtService;

    @Autowired
    public DocumentDataController(DocumentManagementService documentManagementService, JWTService jwtService) {
        this.documentManagementService = documentManagementService;
        this.jwtService = jwtService;
    }


    /*

    GET REQUESTS

     */
    @GetMapping(value = "/getDocuments")
    public List<DocumentData> getUserDocumentData(HttpServletRequest request){
        Long userId = jwtService.getUserId(request);
        return documentManagementService.getAllDocumentData(userId);
    }

    @GetMapping(value="/download/{documentId}")
    public String downloadDataViaUrl(HttpServletRequest request, @PathVariable Long documentId){
        Long userId = jwtService.getUserId(request);
        return documentManagementService.getPresignUrl(userId,documentId);
    }

    @GetMapping(value="/share/{documentId}")
    public String getSharableLink(HttpServletRequest request, @PathVariable Long documentId, @RequestParam long duration){
        Long userId = jwtService.getUserId(request);
        return documentManagementService.getShareableLink(userId, documentId, Duration.ofMinutes(duration));
    }

    @GetMapping(value = "/getDocuments/filter")
    public List<DocumentData> getAllDocumentDataFilter(HttpServletRequest request,
                                                       @RequestParam(required = false) List<String> categories,
                                                       @RequestParam(required = false) List<String> fileExtensions,
                                                       @RequestParam(required = false) LocalDateTime maxDate,
                                                       @RequestParam(required = false) LocalDateTime minDate,
                                                       @RequestParam(required = false) Boolean favorite,
                                                       @RequestParam(required = false) String documentName){
        Long userId = jwtService.getUserId(request);
        if (maxDate == null){
            maxDate = LocalDateTime.of(294276, 12, 31, 23, 59, 59);
        }
        if (minDate == null){
            minDate = LocalDateTime.of(1, 1, 1, 0, 0, 0);
        }
        return documentManagementService.getAllDocumentDataFilter(userId, categories, fileExtensions, maxDate, minDate, favorite, documentName);
    }

      /*

    PUT REQUESTS

     */

    @PutMapping(value="/update/{documentId}")
    public void updateDocumentData(HttpServletRequest request,
                                   @PathVariable Long documentId,
                                   @Valid @RequestBody UpdateDocumentData newDocumentData){
        Long userId = jwtService.getUserId(request);
        documentManagementService.updateDocumentData(userId, documentId, newDocumentData);
    }

     /*

    POST REQUESTS

     */

    @PostMapping(value="/upload", consumes = "multipart/form-data")
    public void uploadDocument(HttpServletRequest request,
                               @Valid @RequestPart("uploadData") UploadDocumentData uploadDocumentData,
                               @RequestPart("file") MultipartFile file) throws IOException {
        Long userId = jwtService.getUserId(request);
        documentManagementService.uploadFile(userId, uploadDocumentData, file);
    }
     /*

    Delete REQUESTS

     */
    @DeleteMapping(value = "/delete/{documentId}")
    public void deleteDocument(HttpServletRequest request, @PathVariable Long documentId){
        Long userId = jwtService.getUserId(request);
        documentManagementService.deleteFile(userId, documentId);
    }
}
