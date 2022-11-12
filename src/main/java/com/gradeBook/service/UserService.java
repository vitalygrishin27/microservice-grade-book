package com.gradeBook.service;

import com.gradeBook.converter.UserConverter;
import com.gradeBook.entity.*;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.InvalidUserPasswordException;
import com.gradeBook.exception.UserNotFoundException;
import com.gradeBook.repository.UserRepo;
import com.gradeBook.service.impl.ClazzServiceImpl;
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
    private final AccessLevelService accessLevelService;
    private final ClazzServiceImpl clazzService;

    private final UserRepo userRepo;

    public List<UserBom> findAll(Boolean needToSort) {
        List<UserBom> result = userConverter.toUserBom(userRepo.findAll());
        if (!needToSort) return result;
        return result.stream()
                .sorted(Comparator.comparing(UserBom::getLastName)
                        .thenComparing(UserBom::getFirstName)
                        .thenComparing(UserBom::getSecondName)).collect(Collectors.toList());
    }

    public List<UserBom> findAll(AccessLevel.LEVEL level, Boolean needToSort) {
        List<UserBom> result = userConverter.toUserBom(userRepo.findByAccessLevel_Level(level));
        if (!needToSort) return result;
        return result.stream()
                .sorted(Comparator.comparing(UserBom::getLastName)
                        .thenComparing(UserBom::getFirstName)
                        .thenComparing(UserBom::getSecondName)).collect(Collectors.toList());
    }

    public UserBom create(UserBom userBom) {
        if (userRepo.findByLogin(userBom.getLogin()).isPresent())
            throw new EntityAlreadyExistsException(userBom.getLogin());
        return userConverter.toUserBom(userRepo.saveAndFlush(userConverter.toNewUser(userBom)));
    }

    public UserBom update(UserBom updatedUserBom) {
        Optional<User> userWithSameLogin = userRepo.findByLogin(updatedUserBom.getLogin());
        if (userWithSameLogin.isPresent() && !userWithSameLogin.get().getOID().equals(updatedUserBom.getOID()))
            throw new UserNotFoundException(updatedUserBom.getLogin());
        Optional<User> userFromDBOptional = userRepo.findById(updatedUserBom.getOID());
        if (userFromDBOptional.isEmpty())
            throw new UserNotFoundException(updatedUserBom.getLogin() + "(id=" + updatedUserBom.getOID() + ")");
        User userFromDB = userFromDBOptional.get();
        userFromDB.setLastName(updatedUserBom.getLastName());
        userFromDB.setFirstName(updatedUserBom.getFirstName());
        userFromDB.setSecondName(updatedUserBom.getSecondName());
        userFromDB.setLogin(updatedUserBom.getLogin());
        userFromDB.setPassword(updatedUserBom.getPassword().equals(userFromDB.getPassword()) ?
                userFromDB.getPassword() :
                encryptPassword(updatedUserBom.getLogin()));
        userFromDB.setAccessLevel(accessLevelService.findByLevel(AccessLevel.LEVEL.valueOf(updatedUserBom.getAccessLevel())));
        switch (userFromDB.getAccessLevel().getLevel()) {
            case TEACHER -> {
                if (updatedUserBom.getClazz() != null) {
                    ((Teacher) userFromDB).setClassFormMaster(clazzService.findById(updatedUserBom.getClazz().getOID()));
                }
            }
            case PUPIL -> ((Pupil) userFromDB).setClazz(clazzService.findById(updatedUserBom.getClazz().getOID()));
        }
        return userConverter.toUserBom(userRepo.saveAndFlush(userFromDB));
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
