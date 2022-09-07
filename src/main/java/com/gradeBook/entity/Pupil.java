package com.gradeBook.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PUPIL")
public class Pupil extends User {
    @ManyToOne
    @JoinColumn(name = "FK_CLAZZ_OID")
    private Clazz clazz;

    @OneToOne(mappedBy = "pupil")
    private Watcher watcher;

    @OneToMany(mappedBy = "pupil")
    private Set<Mark> marks;
}
