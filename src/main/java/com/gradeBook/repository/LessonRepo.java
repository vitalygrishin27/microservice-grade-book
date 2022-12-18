package com.gradeBook.repository;

import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Long> {
    List<Lesson> findByClazz(Clazz clazz);

    @Transactional
    @Modifying
        //   @Query(value = "Delete from Lesson l where l.clazz = :clazz")
    void deleteByClazz(Clazz clazz);
}
