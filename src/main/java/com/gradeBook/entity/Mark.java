package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MARK")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @Override
    public String toString() {
        return "Mark{" +
                "OID=" + OID +
                ", value=" + value +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
