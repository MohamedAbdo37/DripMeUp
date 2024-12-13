package edu.alexu.cse.dripmeup.Entity;

import edu.alexu.cse.dripmeup.enumeration.Gender;
import edu.alexu.cse.dripmeup.enumeration.Role;
import edu.alexu.cse.dripmeup.enumeration.Theme;
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
