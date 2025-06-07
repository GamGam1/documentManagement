package com.gamyA.documentManagement.repository;

import com.gamyA.documentManagement.entity.DocumentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DocumentManagementRepo extends JpaRepository<DocumentData, Long>{

    Optional<DocumentData> findByDocumentIdAndUserId(Long documentId, Long userId);

    List<DocumentData> findByUserIdAndCategory(Long userId, String category);

    void deleteByUserIdAndCategory(Long userId, String category);

    List<DocumentData> findByUserId(Long userId);

    @Query("SELECT d FROM documentData d " +
            "WHERE d.userId = :userId " +
            "AND (:categories IS NULL OR d.category IN :categories) AND (:fileExtensions IS NULL OR  d.fileExtension IN :fileExtensions) " +
            "AND (:favorite IS NULL OR d.favorite = :favorite) " +
            "AND (d.uploadDate >= :minDate AND d.uploadDate <= :maxDate) " +
            "AND (:name IS NULL OR d.documentName LIKE %:name%)")
    List<DocumentData> findByUserIdFilter(@Param("userId") Long userId
            , @Param("categories")List<String> categories, @Param("fileExtensions") List<String> fileExtensions
            , @Param("minDate") LocalDateTime minDate, @Param("maxDate") LocalDateTime maxDate
            , @Param("favorite") Boolean favorite, @Param("name") String name);



}
