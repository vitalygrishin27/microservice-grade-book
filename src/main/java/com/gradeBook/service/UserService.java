package com.gradeBook.service;

import com.gradeBook.entity.Token;
import com.gradeBook.entity.User;
import com.gradeBook.exception.InvalidUserPasswordException;
import com.gradeBook.exception.UserNotFoundException;
import com.gradeBook.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    private User findByLogin(String login) {
        return userRepo.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    public User getUserIfLoginAndPassIsCorrect(String login, String password) {
        User user = findByLogin(login);
        if (!isPasswordMatch(password, user.getPassword())) throw new InvalidUserPasswordException(login);
        return user;
    }

    public static String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public boolean isPasswordMatch(String password, String encryptedPassword) {
        return new String(Base64.getDecoder().decode(encryptedPassword)).equals(password);
    }
}
