package edu.alexu.cse.dripmeup.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ADMIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity{


    @Id
    @Column(name = "user_name")
    private String userName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private int gender;
}
