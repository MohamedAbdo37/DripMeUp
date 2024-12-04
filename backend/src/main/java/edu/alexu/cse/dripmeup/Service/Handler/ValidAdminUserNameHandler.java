package edu.alexu.cse.dripmeup.Service.Handler;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class ValidAdminUserNameHandler extends Handler{

    private final String useName;


    private final AdminRepository adminRepository;

    public ValidAdminUserNameHandler(String userName, AdminRepository adminRepository) {
        this.useName = userName;
        this.adminRepository = adminRepository;
        this.handle();
    }

    @Override
    public void handle() {
        if (useName == null || useName.isEmpty()) {
            throw new HandlerException("Invalid admin user name.");
        }
        if (adminRepository.findByUserName(this.useName).isEmpty()) {
            throw new HandlerException("User name already exist.");
        }
    }

    @Override
    public void handleNext() {

    }

}
