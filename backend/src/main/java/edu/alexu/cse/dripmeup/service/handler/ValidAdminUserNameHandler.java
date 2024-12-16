package edu.alexu.cse.dripmeup.service.handler;

import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.AdminRepository;


public class ValidAdminUserNameHandler extends Handler{

    private final String useName;

    private final AdminRepository adminRepository;

    public ValidAdminUserNameHandler(String userName, AdminRepository adminRepository) {
        this.useName = userName;
        this.adminRepository = adminRepository;
    }

    @Override
    public void handle() {
        if (useName == null || useName.isEmpty()) {
            throw new HandlerException("Invalid admin user name.");
        }
        if (adminRepository.findByUserName(this.useName) != null) {
            throw new HandlerException("User name already exist.");
        }
    }

    @Override
    public void handleNext() {
        // there is no more inputs need to be validated
    }

}
