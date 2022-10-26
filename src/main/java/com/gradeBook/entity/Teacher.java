package com.gradeBook.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TEACHER")
public class Teacher extends User {
    @ManyToMany
    @JoinTable(
            name = "clazz_teachers",
            joinColumns = @JoinColumn(name = "TEACHER_OID"),
            inverseJoinColumns = @JoinColumn(name = "CLAZZ_OID"))
    Set<Clazz> classes;
    @OneToMany(mappedBy = "teacher")
    private Set<Lesson> lessons;
}
