package edu.alexu.cse.dripmeup.Service;

import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Handler.ValidAdminUserNameHandler;
import edu.alexu.cse.dripmeup.Service.Builder.AdminPersonBuilder;
import edu.alexu.cse.dripmeup.excpetion.HandlerException;

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
