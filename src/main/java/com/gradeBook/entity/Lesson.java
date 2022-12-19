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
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DAY_OF_WEEK dayOfWeek;
    @Column(name = "order_number")
    private Integer orderNumber;
    @ManyToOne
    @JoinColumn(name = "FK_TEACHER_OID")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "FK_CLAZZ_OID")
    private Clazz clazz;
    @ManyToOne
    @JoinColumn(name = "FK_SUBJECT_OID")
    private Subject subject;
    @OneToMany(mappedBy = "lesson")
    private Set<Mark> marks;

    public enum DAY_OF_WEEK {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "OID=" + OID +
                ", dayOfWeek=" + dayOfWeek +
                ", orderNumber=" + orderNumber +
                '}';
    }
}
