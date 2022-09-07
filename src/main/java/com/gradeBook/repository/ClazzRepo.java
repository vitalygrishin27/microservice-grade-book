package com.gradeBook.repository;

import com.gradeBook.entity.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClazzRepo extends JpaRepository<Clazz, Long> {
}
