package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PUPIL")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pupil extends User {
    @ManyToOne
    @JoinColumn(name = "FK_CLAZZ_OID")
    private Clazz clazz;

    @OneToOne(mappedBy = "pupil")
    private Watcher watcher;

    @OneToMany(mappedBy = "pupil")
    private Set<Mark> marks;

    @Override
    public String toString() {
        return "Pupil{" +
                "clazz=" + (clazz != null ? clazz.getName() : null) +
                '}';
    }
}
