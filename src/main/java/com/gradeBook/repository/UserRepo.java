package com.gradeBook.repository;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    List<User> findByAccessLevel_Level(AccessLevel.LEVEL accessLevel);
}
