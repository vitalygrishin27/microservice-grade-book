package com.gradeBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="CLAZZ")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Clazz {
    @Id
    @GeneratedValue
    private Long OID;
    private String name;
    @OneToOne
    @JoinColumn(name="FK_TEACHER_FORM_MASTER_OID", referencedColumnName = "OID")
    private Teacher formMaster;
    @ManyToMany(mappedBy = "classes")
    Set<Teacher> teachers;
  /*  @OneToMany(mappedBy = "clazz")
    @JsonIgnore
    private Set<Pupil> pupils;*/
    @OneToMany(mappedBy = "clazz")
    @JsonIgnore
    private Set<Lesson> lessons;

    @Override
    public String toString() {
        return "Clazz{" +
                "OID=" + OID +
                ", name='" + name + '\'' +
                '}';
    }
}
