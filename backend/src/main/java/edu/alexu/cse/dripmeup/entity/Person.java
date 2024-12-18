package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.enumeration.Gender;
import edu.alexu.cse.dripmeup.enumeration.Role;
import edu.alexu.cse.dripmeup.enumeration.Theme;
import lombok.Getter;
import lombok.Setter;


public class Person {

    private @Setter @Getter Role role;
    private @Setter @Getter String username;
    private @Setter @Getter String photo;
    // private List<> favorites;
    private @Setter @Getter String description;
    private @Setter @Getter String email;
    private @Setter @Getter String phone;
    private @Setter @Getter Gender gender;
    private @Setter @Getter Theme theme;
    private @Setter @Getter Long sessionID;

    @Override
    public String toString() {

        return this.username + ", " + this.email;
    }

    public String getEmail() {
        return this.email ;
    }

    public String getUsername() {
        return this.username ;
    }


    public void setPhoto(String photo) {
        this.photo = photo ;
    }

    public void setDescription(String description) {
        this.description = description ;

    }

    public void setTheme(Theme theme) {
        this.theme = theme ;
    }

    public void setRole(Role role) {
        this.role = role ;
    }

    public void setUsername(String userName) {
        this.username = userName ;
    }


    public void setEmail(String email) {
        this.email = email ;
    }

    public void setGender(Gender gender) {
        this.gender = gender ;
    }


    public Role getRole() {
        return this.role ;
    }


    public Theme getTheme() {
        return this.theme ;
    }


    public Gender getGender() {
        return this.gender ;
    }


    public String getPhoto() {
        return this.photo ;
    }


    public String getDescription() {
        return this.description ;
    }


}
