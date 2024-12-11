package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Excpetion.HandlerException;
import edu.alexu.cse.dripmeup.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.Service.Builder.UserPersonBuilder;

@Service
public class UserService {

    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public Person signup(UserEntity user) throws HandlerException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new HandlerException("Email already exists");
        }
        return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
    }

    public boolean logInWithoutPassword(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null;
    }

    // public boolean changePassword(String email, UserEntity newPassword) {
    // UserEntity user = userRepository.findByEmail(email);
    // // changing password goes here
    // return true;
    // }

    public boolean changePassword(String email, UserEntity newPassword) {
        return true;
    }
}
