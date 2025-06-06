package com.gamyA.documentManagement.repository;

import com.gamyA.documentManagement.entity.documentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface documentDataRepo extends JpaRepository<documentData, Long>{

    documentData findByDocumentId(Long documentId);

    List<documentData> findByUserIdAndCategory(Long userId, String category);

    void deleteByUserIdAndCategory(Long userId, String category);

    List<documentData> findByUserId(Long userId);

    @Query("SELECT d FROM documentData d " +
            "WHERE d.userId = :userId " +
            "AND (:categories IS NULL OR d.category IN :categories) AND (:contentTypes IS NULL OR  d.contentType IN :contentTypes) " +
            "AND (:favorite IS NULL OR d.favorite = :favorite) " +
            "AND (d.uploadDate >= :minDate AND d.uploadDate <= :maxDate)")
    List<documentData> findByUserIdFilter(@Param("userId") Long userId
            , @Param("categories")List<String> categories, @Param("contentTypes") List<String> contentTypes
            ,@Param("minDate") LocalDateTime minDate, @Param("maxDate") LocalDateTime maxDate
            ,@Param("favorite") Boolean favorite);

}
