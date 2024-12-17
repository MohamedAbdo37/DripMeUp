package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.repository.AdminRepository;

public class AdminPersonBuilder extends PersonBuilder {

    private final AdminRepository adminRepository;

    private final AdminEntity admin;

    public AdminPersonBuilder(AdminEntity admin, AdminRepository adminRepository) {
        super();
        this.admin = admin;
        this.adminRepository = adminRepository;
    }

    @Override
    public void build() {
        super.buildUserName(admin.getUserName());
        adminRepository.save(admin);
    }

}
