package edu.alexu.cse.dripmeup.Entity;

import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Enumeration.Theme;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Person {

    private Role role;
    private String username;
    private String photo;
    // private List<> favorites;
    private String description;
    private String email;
    private String phone;
    private Gender gender;
    private Theme theme;
    private long sessionID;

    @Override
    public String toString() {

        return this.username + ", " + this.email;
    }
}
