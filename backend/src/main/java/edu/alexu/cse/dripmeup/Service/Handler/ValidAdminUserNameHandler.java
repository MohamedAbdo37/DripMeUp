package edu.alexu.cse.dripmeup.Service.Handler;

import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidAdminUserNameHandler extends Handler{

    private final String useName;

    @Autowired
    private AdminRepository adminRepository;

    public ValidAdminUserNameHandler(String userName) {
        this.useName = userName;
        this.handle();
    }

    @Override
    public void handle() {
        if (useName == null || useName.isEmpty()) {
            throw new HandlerException("Invalid admin user name.");
        }
        assert adminRepository != null;
        if (!adminRepository.findByUserName(this.useName).isEmpty()) {
            throw new HandlerException("User name already exist.");
        }
    }

    @Override
    public void handleNext() {

    }

}
