package com.gradeBook.service;

import com.gradeBook.converter.UserConverter;
import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.Teacher;
import com.gradeBook.entity.User;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.InvalidUserPasswordException;
import com.gradeBook.exception.UserNotFoundException;
import com.gradeBook.repository.ClazzRepo;
import com.gradeBook.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserConverter userConverter;

    private final UserRepo userRepo;
    private final ClazzRepo clazzRepo;

    public List<UserBom> findAll(Boolean needToSort) {
        List<UserBom> result = userConverter.toBom(userRepo.findAll());
        if (!needToSort) return result;
        return result.stream()
                .sorted(Comparator.comparing(UserBom::getLastName)
                        .thenComparing(UserBom::getFirstName)
                        .thenComparing(UserBom::getSecondName)).collect(Collectors.toList());
    }

    public List<UserBom> findAll(AccessLevel.LEVEL level, Boolean needToSort) {
        List<UserBom> result = userConverter.toBom(userRepo.findByAccessLevel_Level(level));
        if (!needToSort) return result;
        return result.stream()
                .sorted(Comparator.comparing(UserBom::getLastName)
                        .thenComparing(UserBom::getFirstName)
                        .thenComparing(UserBom::getSecondName)).collect(Collectors.toList());
    }

    public UserBom create(UserBom userBom) {
        if (userRepo.findByLogin(userBom.getLogin()).isPresent())
            throw new EntityAlreadyExistsException(userBom.getLogin());
        return userConverter.toBom(userRepo.saveAndFlush(userConverter.fromBom(userBom)));
    }

    public UserBom update(UserBom updatedUserBom) {
        Optional<User> userWithSameLogin = userRepo.findByLogin(updatedUserBom.getLogin());
        if (userWithSameLogin.isPresent() && !userWithSameLogin.get().getOID().equals(updatedUserBom.getOID()))
            throw new UserNotFoundException(updatedUserBom.getLogin());
        Optional<User> userFromDBOptional = userRepo.findById(updatedUserBom.getOID());
        if (userFromDBOptional.isEmpty())
            throw new UserNotFoundException(updatedUserBom.getLogin() + "(id=" + updatedUserBom.getOID() + ")");
        return userConverter.toBom(userRepo.saveAndFlush(userConverter.fromBom(updatedUserBom)));
    }

    public void delete(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) return;
        if (userOptional.get() instanceof Teacher && ((Teacher) userOptional.get()).getClassFormMaster() != null) {
            Clazz clazz = ((Teacher) userOptional.get()).getClassFormMaster();
            ((Teacher) userOptional.get()).setClassFormMaster(null);
            clazz.setFormMaster(null);
            clazzRepo.saveAndFlush(clazz);
        }
        userRepo.delete(userOptional.get());
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
