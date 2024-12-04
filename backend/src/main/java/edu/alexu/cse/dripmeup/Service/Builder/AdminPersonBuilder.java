package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminPersonBuilder extends Builder {

    @Autowired
    private DatabaseService databaseService;

    private final AdminEntity admin;

    public AdminPersonBuilder(String userName, String password, Gender gender) {
        super();
        super.buildUserName(userName);
        super.buildGender(gender);
        this.admin= (AdminEntity) super.toEntity();
        admin.setPassword(password);
        this.build();
    }

    @Override
    public void build() {
        databaseService.saveOrUpdate(this.admin);
    }

}
