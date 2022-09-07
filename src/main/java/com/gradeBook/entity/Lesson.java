package com.gradeBook.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "LESSSON")
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
    @OneToMany(mappedBy = "lesson")
    private Set<Mark> marks;
}
