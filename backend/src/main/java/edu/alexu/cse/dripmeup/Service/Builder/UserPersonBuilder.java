package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserPersonBuilder extends Builder {

    @Autowired
    private DatabaseService databaseService;

    private final UserEntity user;

    public UserPersonBuilder(UserEntity user) {
        super();
        super.buildRole(Role.USER);
        super.buildUserName(user.getUserName());
        super.buildGender(user.getGender());
        super.buildEmail(user.getEmail());
        super.buildPhoto(user.getPhoto());
        this.user= (UserEntity) super.toEntity();
        this.build();
    }

    @Override
    public void build() {
        databaseService.saveOrUpdate(this.user);
    }

}
