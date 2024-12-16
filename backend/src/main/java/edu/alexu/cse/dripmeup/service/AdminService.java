package edu.alexu.cse.dripmeup.service;

import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.AdminRepository;
import edu.alexu.cse.dripmeup.service.handler.ValidAdminUserNameHandler;
import edu.alexu.cse.dripmeup.service.builder.AdminPersonBuilder;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean adminLogin(String userName, String password) {
        AdminEntity admin = adminRepository.findByUserName(userName);
        return admin != null && admin.getPassword().equals(password);
    }

    public Person createAdmin(AdminEntity newAdmin) {

        try {
            new ValidAdminUserNameHandler(newAdmin.getUserName(), adminRepository).handle();
        } catch (HandlerException e) {
            return null;
        }

        // create new person
        return new PersonDirector().construct(new AdminPersonBuilder(newAdmin, this.adminRepository));
    }
}
