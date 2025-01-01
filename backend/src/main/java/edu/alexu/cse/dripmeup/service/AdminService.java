package edu.alexu.cse.dripmeup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.AdminRepository;
import edu.alexu.cse.dripmeup.service.handler.ValidAdminUserNameHandler;
import edu.alexu.cse.dripmeup.service.builder.AdminPersonBuilder;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean adminLogin(String userName, String password) {
        AdminEntity admin = adminRepository.findByUserName(userName);
        return admin != null && passwordEncoder.matches(password, admin.getPassword());
    }

    public Person createAdmin(AdminEntity newAdmin) {

        try {
            new ValidAdminUserNameHandler(newAdmin.getUserName(), adminRepository).handle();
        } catch (HandlerException e) {
            return null;
        }

        // create new person
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        return new PersonDirector().construct(new AdminPersonBuilder(newAdmin, this.adminRepository));
    }
}
