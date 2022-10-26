package com.gradeBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TOKEN")
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
}
