package edu.alexu.cse.dripmeup.Entity;

import edu.alexu.cse.dripmeup.Enumeration.Theme;
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
public class AdminEntity implements EntityIF {

    @Id
    @Column(name = "admin_id")
    private long userID;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "theme")
    private Theme theme;
}
