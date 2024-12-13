package edu.alexu.cse.dripmeup.Entity;

import java.util.Date;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "code")
    private int code;

    @Column(name = "email")
    private String email;

    // @Column(name = "createdDate")
    // private Date createdDate;

}
