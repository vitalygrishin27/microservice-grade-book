package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TEACHER")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Teacher extends User {
    @ManyToMany
    @JoinTable(
            name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "TEACHER_OID"),
            inverseJoinColumns = @JoinColumn(name = "SUBJECT_OID"))
    Set<Subject> subjects;
    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons;
    @OneToOne(mappedBy = "formMaster")
    private Clazz classFormMaster;

    @Override
    public String toString() {
        return "Teacher{" +
                "classFormMaster=" + (classFormMaster != null ? classFormMaster.getName() : null) +
                '}';
    }
}
