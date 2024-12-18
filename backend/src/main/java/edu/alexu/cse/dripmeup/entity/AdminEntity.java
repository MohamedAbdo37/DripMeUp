package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.enumeration.Theme;
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
@Table(name = "ADMIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity implements EntityIF {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userID;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "theme")
    private Theme theme;

    public void setUserName(String userName) {
        this.userName = userName ;
    }

    public void setPassword(String password) {
        this.password = password ;
    }

    public String getUserName() {
        return this.userName ;
    }

    public void setTheme(Theme theme) {
        this.theme = theme ;
    }

    public String getPassword() {
        return this.password ;
    }
}
