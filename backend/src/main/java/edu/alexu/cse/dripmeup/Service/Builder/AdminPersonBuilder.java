package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.DatabaseService;

public class AdminPersonBuilder extends Builder {

    private DatabaseService databaseService;
    private AdminRepository adminRepository;

    private final AdminEntity admin;
    private final boolean repo;

    public AdminPersonBuilder(AdminEntity admin, DatabaseService databaseService) {
        super();
        this.admin = admin;
        this.databaseService = databaseService;
        this.repo = false;
    }

    public AdminPersonBuilder(AdminEntity admin, AdminRepository adminRepository) {
        super();
        this.admin = admin;
        this.adminRepository = adminRepository;
        this.repo = true;
    }

    @Override
    public void build() {
        super.buildUserName(admin.getUserName());
        super.buildGender(admin.getGender());
        if (this.repo) {
            adminRepository.save(admin);
        } else {
            databaseService.saveOrUpdate(admin);
        }
    }

}
