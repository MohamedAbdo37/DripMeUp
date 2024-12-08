package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.DatabaseService;

public class UserPersonBuilder extends Builder {

    private DatabaseService databaseService = null;
    private UserRepository userRepository = null;


    private final UserEntity user;
    private boolean repo;

    public UserPersonBuilder(UserEntity user, DatabaseService databaseService) {
        super();
        this.user = user;
        this.databaseService = databaseService;
        this.repo = false;
    }

    public UserPersonBuilder(UserEntity user, UserRepository userRepository) {
        super();
        this.user = user;
        this.userRepository = userRepository;
        this.repo = true;
    }

    @Override
    public void build() {
        super.buildRole(Role.USER);
        super.buildName(user.getUserName());
        super.buildGender(user.getGender());
        super.buildEmail(user.getEmail());
        super.buildPhoto(user.getPhoto());
        super.buildDescription(user.getDescription());
        if(this.repo) userRepository.save(this.user);
        else databaseService.saveOrUpdate(this.user);
    }

}
