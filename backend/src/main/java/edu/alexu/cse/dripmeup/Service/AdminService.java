package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public boolean adminLogin(String userName, String password) {
        AdminEntity admin = adminRepository.findByUserName(userName);
        return admin != null && admin.getPassword().equals(password);
    }
}
