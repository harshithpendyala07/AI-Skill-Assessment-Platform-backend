package com.aiskill.platform.repository;

import com.aiskill.platform.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findBySkillIgnoreCase(String skill);
    List<Test> findByDifficultyIgnoreCase(String difficulty);
}
