package com.gradeBook.converter;

import com.gradeBook.entity.*;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.service.AccessLevelService;
import com.gradeBook.service.impl.ClazzServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.gradeBook.service.UserService.encryptPassword;

@Service
@RequiredArgsConstructor
public class UserConverter {
    private final AccessLevelService accessLevelService;
    private final ClazzServiceImpl clazzService;
    private final ClazzConverter clazzConverter;

    public UserBom toUserBom(User source) {
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
            result.setClazz(clazzConverter.toClazzBom(((Pupil) source).getClazz()));

        if (source instanceof Teacher)
            if ((((Teacher) source).getClassFormMaster() != null))
                result.setClazz(clazzConverter.toClazzBom(((Teacher) source).getClassFormMaster()));

        return result;
    }

    public List<UserBom> toUserBom(List<User> source) {
        List<UserBom> result = new ArrayList<>();
        if (source == null) return result;
        source.forEach(user -> result.add(toUserBom(user)));
        return result;
    }

    public User toNewUser(UserBom source) {
        if (source == null) return null;
        User result = switch (AccessLevel.LEVEL.valueOf(source.getAccessLevel())) {
            case TEACHER -> new Teacher(null, null, source.getClazz() == null ? null : clazzService.findById(source.getClazz().getOID()));
            case PUPIL -> new Pupil(clazzService.findById(source.getClazz().getOID()), null, null);
            default -> new Watcher();
        };

        result.setOID(source.getOID());
        result.setLastName(source.getLastName());
        result.setFirstName(source.getFirstName());
        result.setSecondName(source.getSecondName());
        result.setLogin(source.getLogin());
        result.setPassword(encryptPassword(source.getPassword()));
        result.setAccessLevel(accessLevelService.findByLevel(AccessLevel.LEVEL.valueOf(source.getAccessLevel())));
        return result;
    }
}
