package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminPersonBuilder extends Builder {

    @Autowired
    private DatabaseService databaseService;

    private final AdminEntity admin;

    public AdminPersonBuilder(AdminEntity admin) {
        super();
        super.buildUserName(admin.getUserName());
        super.buildGender(admin.getGender());
        this.admin= (AdminEntity) super.toEntity();
        admin.setPassword(admin.getPassword());
        this.build();
    }

    @Override
    public void build() {
        databaseService.saveOrUpdate(this.admin);
    }

}
