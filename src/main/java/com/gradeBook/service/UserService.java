package com.gradeBook.service;

import com.gradeBook.converter.UserConverter;
import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.User;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.InvalidUserPasswordException;
import com.gradeBook.exception.UserNotFoundException;
import com.gradeBook.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserConverter userConverter;

    private final UserRepo userRepo;

    public List<UserBom> findAll() {
        return userConverter.toUserBom(userRepo.findAll());
    }

    public List<UserBom> findAll(AccessLevel.LEVEL level) {
        return userConverter.toUserBom(userRepo.findByAccessLevel_Level(level));
    }

    public UserBom create(UserBom userBom) {
        if (userRepo.findByLogin(userBom.getLogin()).isPresent())
            throw new EntityAlreadyExistsException(userBom.getLogin());
        User newUser = userConverter.toUser(userBom);
        newUser.setPassword(encryptPassword(newUser.getPassword()));
        return userConverter.toUserBom(userRepo.saveAndFlush(newUser));
    }

    public UserBom update(UserBom updatedUserBom) {
        Optional<User> userWithSameLogin = userRepo.findByLogin(updatedUserBom.getLogin());
        if (userWithSameLogin.isPresent() && !userWithSameLogin.get().getOID().equals(updatedUserBom.getOID()))
            throw new UserNotFoundException(updatedUserBom.getLogin());
        Optional<User> userFromDBOptional = userRepo.findById(updatedUserBom.getOID());
        if (userFromDBOptional.isEmpty())
            throw new UserNotFoundException(updatedUserBom.getLogin() + "(id=" + updatedUserBom.getOID() + ")");
        User userFromDB = userFromDBOptional.get();
        UserBom userBomFormDB = userConverter.toUserBom(userFromDB);
        userBomFormDB.setLastName(updatedUserBom.getLastName());
        userBomFormDB.setFirstName(updatedUserBom.getFirstName());
        userBomFormDB.setSecondName(updatedUserBom.getSecondName());
        userBomFormDB.setLogin(updatedUserBom.getLogin());
        userBomFormDB.setPassword(updatedUserBom.getPassword().equals(userBomFormDB.getPassword()) ?
                userBomFormDB.getPassword() :
                encryptPassword(updatedUserBom.getLogin()));
        userBomFormDB.setAccessLevel(updatedUserBom.getAccessLevel());
        userBomFormDB.setClazzId(updatedUserBom.getClazzId());
        return userConverter.toUserBom(userRepo.saveAndFlush(userConverter.toUser(userBomFormDB)));
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
