package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ACCESS_LEVEL")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessLevel {
    @Id
    @GeneratedValue
    private Long OID;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private LEVEL level;

    public enum LEVEL {
        ADMIN, TEACHER, PUPIL, BASIC
    }
}
