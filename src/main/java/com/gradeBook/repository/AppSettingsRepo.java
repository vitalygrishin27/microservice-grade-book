package com.gradeBook.repository;

import com.gradeBook.entity.AppSettings;
import com.gradeBook.entity.Token;
import com.gradeBook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppSettingsRepo extends JpaRepository<AppSettings, Long> {
    AppSettings findByName(String name);
}
