package com.gradeBook.repository;

import com.gradeBook.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PupilRepo extends JpaRepository<Pupil, Long> {
}
