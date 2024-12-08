package edu.alexu.cse.dripmeup.Entity;

import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Enumeration.Theme;
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
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements EntityIF{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "userID")
    private long userID;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "photo")
    private String photo;

    @Column(name = "description")
    private String description;
    
    @Column(name = "theme")
    private Theme theme;

    
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Gender getGender() {
        return this.gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
