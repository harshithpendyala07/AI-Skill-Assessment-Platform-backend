package com.aiskill.platform.repository;

import com.aiskill.platform.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
    List<DocumentChunk> findByDocumentId(Long documentId);

    @Query("SELECT c FROM DocumentChunk c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.keywords) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<DocumentChunk> searchChunks(String query);
}
