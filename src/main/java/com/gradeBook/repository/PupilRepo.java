package com.gradeBook.repository;

import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PupilRepo extends JpaRepository<Pupil, Long> {
    List<Pupil> findByClazz(Clazz clazz);
}
