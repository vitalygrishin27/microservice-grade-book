package com.gradeBook.service;

import com.gradeBook.converter.UserConverter;
import com.gradeBook.entity.*;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.exception.*;
import com.gradeBook.repository.ClazzRepo;
import com.gradeBook.repository.LessonRepo;
import com.gradeBook.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final LessonRepo lessonRepo;

    public List<UserBom> findAll(Boolean needToSort, String search) {
        List<UserBom> result = userConverter.toBom(userRepo.findAll());
        if (!search.isEmpty()) result = filterResult(result, search);
        if (!needToSort) return result;
        return result.stream()
                .sorted(Comparator.comparing(UserBom::getLastName)
                        .thenComparing(UserBom::getFirstName)
                        .thenComparing(UserBom::getSecondName)).collect(Collectors.toList());
    }

    public List<UserBom> findAll(AccessLevel.LEVEL level, Boolean needToSort, String search) {
        List<UserBom> result = userConverter.toBom(userRepo.findByAccessLevel_Level(level));
        if (!search.isEmpty()) result = filterResult(result, search);
        if (!needToSort) return result;
        return result.stream()
                .sorted(Comparator.comparing(UserBom::getLastName)
                        .thenComparing(UserBom::getFirstName)
                        .thenComparing(UserBom::getSecondName)).collect(Collectors.toList());
    }

    public UserBom create(UserBom userBom) {
        checkFieldsForBlanks(userBom);
        if (userRepo.findByLogin(userBom.getLogin()).isPresent())
            throw new EntityAlreadyExistsException(userBom.getLogin());
        return userConverter.toBom(userRepo.saveAndFlush(userConverter.fromBom(userBom)));
    }

    public UserBom update(UserBom updatedUserBom) {
        checkFieldsForBlanks(updatedUserBom);
        Optional<User> userWithSameLogin = userRepo.findByLogin(updatedUserBom.getLogin());
        if (userWithSameLogin.isPresent() && !userWithSameLogin.get().getOID().equals(updatedUserBom.getOID()))
            throw new UserNotFoundException(updatedUserBom.getLogin());
        Optional<User> userFromDBOptional = userRepo.findById(updatedUserBom.getOID());
        if (userFromDBOptional.isEmpty())
            throw new UserNotFoundException(updatedUserBom.getLogin() + "(id=" + updatedUserBom.getOID() + ")");
        checkSchedulerForTeacher(userFromDBOptional.get(), updatedUserBom);
        return userConverter.toBom(userRepo.saveAndFlush(userConverter.fromBom(updatedUserBom)));
    }

    @Transactional
    public void delete(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) return;
        if (userOptional.get() instanceof Teacher && ((Teacher) userOptional.get()).getClassFormMaster() != null) {
            Clazz clazz = ((Teacher) userOptional.get()).getClassFormMaster();
            ((Teacher) userOptional.get()).setClassFormMaster(null);
            clazz.setFormMaster(null);
            clazzRepo.save(clazz);
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

    private List<UserBom> filterResult(List<UserBom> users, String search) {
        return users.stream().filter(userBom ->
                userBom.getLastName().toLowerCase().contains(search.toLowerCase()) ||
                        userBom.getFirstName().toLowerCase().contains(search.toLowerCase()) ||
                        userBom.getSecondName().toLowerCase().contains(search.toLowerCase()) ||
                        userBom.getLogin().toLowerCase().contains(search.toLowerCase()) ||
                        (userBom.getClazz() != null && userBom.getClazz().getName().toLowerCase().contains(search.toLowerCase()))).collect(Collectors.toList());
    }

    private void checkFieldsForBlanks(UserBom userBom) {
        if (userBom.getLastName().isBlank() ||
                userBom.getFirstName().isBlank() ||
                userBom.getLogin().isBlank() ||
                userBom.getPassword().isBlank())
            throw new EntityIsInvalidException();
    }

    private void checkSchedulerForTeacher(User user, UserBom updatedUser) {
        if (!(user instanceof Teacher teacher)) return;
        List<Lesson> lessons = lessonRepo.findByTeacher(teacher);
        if (!lessons.isEmpty() && updatedUser.getSelectedSubjects().size() == 0) {
            throw new SchedulerHasDependencyException();
        }
        lessons.forEach(lesson -> {
            if (!updatedUser.getSelectedSubjects().contains(lesson.getSubject().getOID())) {
                throw new SchedulerHasDependencyException();
            }
        });
    }
}
