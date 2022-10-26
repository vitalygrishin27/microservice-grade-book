package com.gradeBook.repository;

import com.gradeBook.entity.AccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLevelRepo extends JpaRepository<AccessLevel, Long> {
}
