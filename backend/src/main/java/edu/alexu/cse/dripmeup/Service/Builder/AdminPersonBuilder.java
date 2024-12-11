package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;

public class AdminPersonBuilder extends Builder {

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
