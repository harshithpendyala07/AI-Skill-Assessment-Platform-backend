package com.aiskill.platform.repository;

import com.aiskill.platform.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Result> findTop20ByOrderByScoreDescCreatedAtDesc();

    @Query("SELECT AVG(r.score) FROM Result r WHERE r.user.id = :userId")
    Double findAverageScoreByUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.user.id = :userId")
    Long countTestsTakenByUserId(Long userId);

    long deleteByUserId(Long userId);

    @Query("SELECT AVG(r.score) FROM Result r")
    Double findOverallAverageScore();

    @Query("SELECT COUNT(DISTINCT r.user.id) FROM Result r")
    Long countStudentsWithResults();
}
