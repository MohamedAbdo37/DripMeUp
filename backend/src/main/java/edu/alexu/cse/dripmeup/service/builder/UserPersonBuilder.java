package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.enumeration.Role;

public class UserPersonBuilder extends PersonBuilder {

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
