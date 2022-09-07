package com.gradeBook.repository;

import com.gradeBook.entity.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatcherRepo extends JpaRepository<Watcher, Long> {
}
