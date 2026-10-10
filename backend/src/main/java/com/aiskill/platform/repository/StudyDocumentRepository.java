package com.aiskill.platform.repository;

import com.aiskill.platform.model.StudyDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyDocumentRepository extends JpaRepository<StudyDocument, Long> {
    List<StudyDocument> findByCategoryIgnoreCase(String category);
}
