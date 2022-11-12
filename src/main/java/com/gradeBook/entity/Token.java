package com.gradeBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TOKEN")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {

    @Id
    @GeneratedValue
    private Long OID;
    private String token;
    @OneToOne
    @JoinColumn(name = "FK_USER_OID", referencedColumnName = "OID")
    @JsonIgnore
    private User user;
    private LocalDateTime validTo;
    public boolean isValid() {
        return this.validTo.isAfter(LocalDateTime.now());
    }
    @Transient
    private String firstName;
    @Transient
    private String accessLevel;
}
