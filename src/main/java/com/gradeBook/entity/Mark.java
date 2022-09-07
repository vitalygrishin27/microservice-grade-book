package com.gradeBook.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MARK")
public class Mark {
    @Id
    @GeneratedValue
    private Long OID;
    private Integer value;
    private LocalDateTime localDateTime;
    @ManyToOne
    @JoinColumn(name="FK_PUPIL_OID")
    private Pupil pupil;
    @ManyToOne
    @JoinColumn(name="FK_LESSON_OID")
    private Lesson lesson;
    @ManyToOne
    @JoinColumn(name="FK_TEACHER_OID")
    private Teacher teacher;
}
