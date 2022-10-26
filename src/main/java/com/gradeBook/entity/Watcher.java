package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WATCHER")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Watcher extends User{
    @OneToOne
    @JoinColumn(name = "FK_PUPIL_OID", referencedColumnName = "OID")
    private  Pupil pupil;

}
