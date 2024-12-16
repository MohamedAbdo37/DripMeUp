package edu.alexu.cse.dripmeup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.service.builder.UserPersonBuilder;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
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

    public boolean isEmailPresent(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null;
    }

    public boolean changePassword(String email, UserEntity newPassword) {
        UserEntity user = userRepository.findByEmail(email);
        // changing password goes here
        user.setPassword(newPassword.getPassword());
        this.userRepository.save(user);

        return true;
    }


}
