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
}
