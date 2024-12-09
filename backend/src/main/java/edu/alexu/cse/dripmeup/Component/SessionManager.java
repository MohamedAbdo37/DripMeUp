package edu.alexu.cse.dripmeup.component;

import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.AdminService;

@Component
public class SessionManager {

    private final AdminRepository adminRepository;

    public SessionManager(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    public Person adminSignUP(String userName, String password) {
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(password);
        AdminService service = new AdminService(adminRepository);
        return service.createAdmin(admin);
    }
}
