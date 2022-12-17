package com.gradeBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SUBJECT")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subject {
    @Id
    @GeneratedValue
    private Long OID;
    private String name;
    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private Set<Lesson> lessons;
    @ManyToMany(mappedBy = "subjects")
    Set<Teacher> teachers;
}
