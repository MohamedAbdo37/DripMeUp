package edu.alexu.cse.dripmeup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.service.builder.UserPersonBuilder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
//        System.out.println(user.getPassword());
//        System.out.println(passwordEncoder.encode(password));
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public Person signup(UserEntity user) throws HandlerException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new HandlerException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
