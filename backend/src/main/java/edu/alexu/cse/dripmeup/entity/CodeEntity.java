package edu.alexu.cse.dripmeup.entity;


import java.time.Instant;
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

    public Integer getCode() {
        return this.code ;
    }


    public void setCode(int code) {
        this.code = code ;
    }

    public void setEmail(String email) {
        this.email = email ;
    }


    public LocalDateTime getExpiredDate() {
        return this.expiredDate ;
    }

    public void setExpiredDate(LocalDateTime localDateTime) {
        this.expiredDate = localDateTime ;
    }

    public void setCreatedDate(LocalDateTime now) {
        this.createdDate = now ;
    }

    public Long getCodeID() {
        return this.codeID ;
    }
}
