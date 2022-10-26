package com.gradeBook.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "APP_SETTINGS")
@Data
public class AppSettings {
    @Id
    @GeneratedValue
    private Long OID;
    private String name;
    private String description;
    private String value;
}
