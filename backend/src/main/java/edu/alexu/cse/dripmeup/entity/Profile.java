package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.enumeration.Gender;
import edu.alexu.cse.dripmeup.enumeration.Role;
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


    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone ;
    }

    public void setProfilePhoto(String photo) {
        this.profilePhoto = photo ;
    }
}
