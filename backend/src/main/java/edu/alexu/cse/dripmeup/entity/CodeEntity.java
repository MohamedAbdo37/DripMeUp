package edu.alexu.cse.dripmeup.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VerificationCode")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeEntity implements EntityIF {

    @Id
    @Column(name = "codeId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codeID;

    @Column(name = "code")
    private int code;

    @Column(name = "email")
    private String email;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "expiredDate")
    private LocalDateTime expiredDate;

}
