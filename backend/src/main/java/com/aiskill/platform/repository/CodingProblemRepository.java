package com.aiskill.platform.repository;

import com.aiskill.platform.model.CodingProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingProblemRepository extends JpaRepository<CodingProblem, Long> {
    List<CodingProblem> findByDifficultyIgnoreCase(String difficulty);
    List<CodingProblem> findByCategoryIgnoreCase(String category);
}
