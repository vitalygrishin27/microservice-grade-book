package com.gradeBook.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SUBJECT")
public class Subject {
    @Id
    @GeneratedValue
    private Long OID;
    private String name;
    @OneToMany(mappedBy = "subject")
    private Set<Lesson> lessons;
}
