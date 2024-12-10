package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.EntityIF;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.Profile;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.enumeration.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserProfileService {
    UserRepository userRepository;
    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Profile getUserProfile(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return getTransferObject(user);
    }
    public static Profile getTransferObject(UserEntity user){
        if(user == null) return null;
        Profile profile = new Profile();
        profile.setRole(Role.USER);
        profile.setUsername(user.getUserName());
        profile.setEmail(user.getEmail());
        profile.setGender(user.getGender());
        profile.setPhoneNumber(user.getPhone());
        profile.setProfilePhoto(user.getPhoto());
        return profile;
    }
    public byte[] getUserProfilePhoto(String id) {
        return new byte[0];
    }
    public String changeUserProfilePhoto(String id, byte[] photo) {
        return "POST request received with body: ";
    }

    public String changeUsername(String id, String name) {
        return "POST request received with body: ";
    }
}
