package edu.alexu.cse.dripmeup.Entity;

import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Enumeration.Theme;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Person {
    // SETTERS
    // GETTERS
    private Role role;
    private int personID;
    private String username;
    private String name;
    private String photo;
//    private List<> favorites;
    private String description;
    private String email;
    private String phone;
//    private Address address;
    private Gender gender;
    private Theme theme;
    private Date Bdate;

}
