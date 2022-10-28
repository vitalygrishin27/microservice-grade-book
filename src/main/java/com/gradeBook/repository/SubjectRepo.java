package com.gradeBook.repository;

import com.gradeBook.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {
    Subject findByName(String name);
}
