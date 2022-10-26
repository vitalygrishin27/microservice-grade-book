package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "USER_DATA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue
    private Long OID;
    private String firstName;
    private String secondName;
    private String lastName;
    private String login;
    private String password;
    @ManyToOne
    @JoinColumn(name = "FK_ACCESS_LEVEL_OID")
    private AccessLevel accessLevel;
    @OneToOne(mappedBy = "user")
    private Token token;

    @Override
    public String toString() {
        return "User{" +
                "OID=" + OID +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", accessLevel=" + accessLevel +
                ", token=" + (token == null ? null : token.getToken()) +
                '}';
    }
}
