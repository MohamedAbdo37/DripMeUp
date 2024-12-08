package edu.alexu.cse.dripmeup.component;

import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;

@Component
public class SessionManager {

    private final AdminRepository adminRepository;

    public SessionManager(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    public Person adminSignUP(String userName, String password) {

        return null;
    }
}
