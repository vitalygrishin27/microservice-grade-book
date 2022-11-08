package com.gradeBook.converter;

import com.gradeBook.entity.User;
import com.gradeBook.entity.bom.UserBom;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserConverter {
    public UserBom toUserBom(User source) {
        if (source == null) return null;
        UserBom result = new UserBom();
        result.setLastName(source.getLastName());
        result.setFirstName(source.getFirstName());
        result.setSecondName(source.getSecondName());
        result.setLogin(source.getLogin());
        result.setPassword(source.getPassword());
        result.setAccessLevel(source.getAccessLevel().getName());
        result.setClazz("");
        return result;
    }

    public List<UserBom> toUserBom(List<User> source) {
        List<UserBom> result = new ArrayList<>();
        if (source == null) return result;
        source.forEach(user -> result.add(toUserBom(user)));
        return result;
    }
}
