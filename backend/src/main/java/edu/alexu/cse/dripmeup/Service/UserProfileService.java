package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    UserRepository userRepository;
    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Person getUserProfile(String id) {
        return new Person();
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
