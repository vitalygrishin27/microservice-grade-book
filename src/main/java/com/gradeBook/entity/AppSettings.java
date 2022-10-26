package com.gradeBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "APP_SETTINGS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppSettings {
    @Id
    @GeneratedValue
    private Long OID;
    private String name;
    private String description;
    private String value;
}
