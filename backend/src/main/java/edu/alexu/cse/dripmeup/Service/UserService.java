package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public String signup(UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email already exists";
        }
        userRepository.save(user);
        return "User created successfully";
    }

    public boolean logInWithoutPassword(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null;
    }

    public boolean changePassword(String email, UserEntity newPassword) {
        UserEntity user = userRepository.findByEmail(email);
        // changing password goes here
        return true;
    }
}
