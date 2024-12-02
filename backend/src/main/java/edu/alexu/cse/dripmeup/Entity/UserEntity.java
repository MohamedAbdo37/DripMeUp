package edu.alexu.cse.dripmeup.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "User")

public class UserEntity {
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    private String gender;
    @Column(name = "photo")
    @Lob
    private byte[] photo;
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
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public byte[] getPhoto() {
        return this.photo;
    }
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }


}
