package com.gradeBook.converter;

import com.gradeBook.entity.*;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.exception.ClassAlreadyHasFormMasterException;
import com.gradeBook.exception.EntityIsInvalidException;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.exception.UserNotFoundException;
import com.gradeBook.repository.ClazzRepo;
import com.gradeBook.repository.UserRepo;
import com.gradeBook.service.AccessLevelService;
import com.gradeBook.service.impl.SubjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.gradeBook.service.UserService.encryptPassword;

@Service
@RequiredArgsConstructor
public class UserConverter {
    private final AccessLevelService accessLevelService;
    private final ClazzConverter clazzConverter;
    private final UserRepo userRepo;
    private final ClazzRepo clazzRepo;
    private final SubjectServiceImpl subjectService;

    public UserBom toBom(User source) {
        if (source == null) return null;
        UserBom result = new UserBom();
        result.setOID(source.getOID());
        result.setLastName(source.getLastName());
        result.setFirstName(source.getFirstName());
        result.setSecondName(source.getSecondName());
        result.setLogin(source.getLogin());
        result.setPassword(source.getPassword());
        result.setAccessLevel(source.getAccessLevel().getName());
        result.setClazz(null);
        if (source instanceof Pupil)
            result.setClazz(clazzConverter.toBom(((Pupil) source).getClazz()));

        if (source instanceof Teacher) {
            if ((((Teacher) source).getClassFormMaster() != null))
                result.setClazz(clazzConverter.toBom(((Teacher) source).getClassFormMaster()));
            ((Teacher) source).getSubjects().forEach(subject -> result.getSelectedSubjects().add(subject.getOID()));
        }

        return result;
    }

    public List<UserBom> toBom(List<User> source) {
        List<UserBom> result = new ArrayList<>();
        if (source == null) return result;
        source.forEach(user -> result.add(toBom(user)));
        return result;
    }

    public User fromBom(UserBom source) {
        if (source == null) return null;
        User result;
        switch (AccessLevel.LEVEL.valueOf(source.getAccessLevel())) {
            case TEACHER -> {
                if (source.getOID() != null) {
                    Optional<User> optionalUser = userRepo.findById(source.getOID());
                    if (optionalUser.isEmpty())
                        throw new UserNotFoundException(source.getLogin() + "(id=" + source.getOID() + ")");
                    result = optionalUser.get();
                    Clazz clazz = ((Teacher) result).getClassFormMaster();
                    if (clazz != null) {
                        ((Teacher) result).setClassFormMaster(null);
                        clazz.setFormMaster(null);
                    }
                    Set<Subject> subjects = new HashSet<>();
                    source.getSelectedSubjects().forEach(subjectOid -> {
                        subjects.add(subjectService.findById(subjectOid));
                    });
                    ((Teacher) result).setSubjects(subjects);
                } else {
                    result = new Teacher();
                }
                if (source.getClazz() != null && source.getClazz().getOID() != null) {
                    Optional<Clazz> optionalClazz = clazzRepo.findById(source.getClazz().getOID());
                    if (optionalClazz.isEmpty()) throw new EntityNotFoundException(source.getClazz().getOID());
                    if (optionalClazz.get().getFormMaster() != null)
                        throw new ClassAlreadyHasFormMasterException(optionalClazz.get().getName(), optionalClazz.get().getFormMaster().getLastName());
                    ((Teacher) result).setClassFormMaster(optionalClazz.get());
                    optionalClazz.get().setFormMaster((Teacher) result);
                }
            }
            case PUPIL -> {
                if (source.getOID() != null) {
                    Optional<User> optionalUser = userRepo.findById(source.getOID());
                    if (optionalUser.isEmpty()) throw new EntityNotFoundException(source.getOID());
                    result = optionalUser.get();
                } else {
                    result = new Pupil();
                }
                if (source.getClazz() == null) throw new EntityIsInvalidException();
                Optional<Clazz> optionalClazz = clazzRepo.findById(source.getClazz().getOID());
                if (optionalClazz.isEmpty()) throw new EntityNotFoundException(source.getClazz().getOID());
                ((Pupil) result).setClazz(optionalClazz.get());
            }
            case ADMIN -> {
                if (source.getOID() != null) {
                    Optional<User> optionalUser = userRepo.findById(source.getOID());
                    if (optionalUser.isEmpty()) throw new EntityNotFoundException(source.getOID());
                    result = optionalUser.get();
                } else {
                    result = new Watcher();
                }
            }
            default -> result = new Watcher();
        }
        result.setLastName(source.getLastName());
        result.setFirstName(source.getFirstName());
        result.setSecondName(source.getSecondName());
        result.setLogin(source.getLogin());
        result.setPassword(source.getPassword().equals(result.getPassword()) ?
                result.getPassword() :
                encryptPassword(source.getLogin()));
        result.setAccessLevel(accessLevelService.findByLevel(AccessLevel.LEVEL.valueOf(source.getAccessLevel())));
        return result;
    }
}
