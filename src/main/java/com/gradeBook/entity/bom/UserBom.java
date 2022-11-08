package com.gradeBook.entity.bom;

import com.gradeBook.entity.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBom {
    private Long OID;
    private String firstName;
    private String secondName;
    private String lastName;
    private String login;
    private String password;
    private String accessLevel;
    private Token token;
    private String clazz;
}
