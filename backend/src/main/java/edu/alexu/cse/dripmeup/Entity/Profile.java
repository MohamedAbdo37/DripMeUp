package edu.alexu.cse.dripmeup.Entity;

import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Enumeration.Theme;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Profile {
    private Role role;
    private String username;
    private String profilePhoto;
    private String email;
    private String phoneNumber;
    private Gender gender;
}
