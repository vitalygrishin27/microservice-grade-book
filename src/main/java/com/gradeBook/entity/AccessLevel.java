package com.gradeBook.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCESS_LEVEL")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessLevel {
    @Id
    @GeneratedValue
    private Long OID;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private LEVEL level;

    public enum LEVEL {
        LOGIN, BASIC, ADMIN
    }
   /* @OneToMany(mappedBy = "accessLevel")
    private Set<Token> tokens;*/

}
