package com.gradeBook.repository;

import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.Lesson;
import com.gradeBook.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Long> {
    List<Lesson> findByClazz(Clazz clazz);

    List<Lesson> findByTeacher(Teacher teacher);

    @Transactional
    @Modifying
    void deleteByClazz(Clazz clazz);

   List<Lesson> findByTeacherAndDayOfWeekAndOrderNumber(Teacher teacher, Lesson.DAY_OF_WEEK day_of_week, Integer orderNumber);
}
