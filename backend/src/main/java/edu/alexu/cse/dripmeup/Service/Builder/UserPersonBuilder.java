package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.DatabaseService;
import edu.alexu.cse.dripmeup.Enumeration.Role;

public class UserPersonBuilder extends Builder {

    private final UserRepository userRepository;

    private final UserEntity user;

    public UserPersonBuilder(UserEntity user, UserRepository userRepository) {
        super();
        this.user = user;
        this.userRepository = userRepository;
    }

    @Override
    public void build() {
        super.buildRole(Role.USER);
        super.buildName(user.getUserName());
        super.buildGender(user.getGender());
        super.buildEmail(user.getEmail());
        super.buildPhoto(user.getPhoto());
        super.buildDescription(user.getDescription());
        this.userRepository.save(this.user);
    }

}
