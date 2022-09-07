package com.gradeBook.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WATCHER")
public class Watcher extends User{
    @OneToOne
    @JoinColumn(name = "FK_PUPIL_OID", referencedColumnName = "OID")
    private  Pupil pupil;

}
