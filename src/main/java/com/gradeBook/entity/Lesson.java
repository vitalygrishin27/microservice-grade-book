package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "LESSON")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lesson {
    @Id
    @GeneratedValue
    private Long OID;
    private String Name;
    @ManyToOne
    @JoinColumn(name="FK_TEACHER_OID")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name="FK_CLAZZ_OID")
    private Clazz clazz;
    @ManyToOne
    @JoinColumn(name="FK_SUBJECT_OID")
    private Subject subject;
    @OneToMany(mappedBy = "lesson")
    private Set<Mark> marks;
}
