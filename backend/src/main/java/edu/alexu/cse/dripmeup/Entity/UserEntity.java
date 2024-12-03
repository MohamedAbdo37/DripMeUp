package edu.alexu.cse.dripmeup.Entity;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String email;
    private String password;
    private String username;
    private boolean gender;
    private String photoPath;

}
