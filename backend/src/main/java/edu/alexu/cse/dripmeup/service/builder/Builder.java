package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.service.DatabaseService;
import edu.alexu.cse.dripmeup.enumeration.Gender;
import edu.alexu.cse.dripmeup.enumeration.Role;
import edu.alexu.cse.dripmeup.enumeration.Theme;

public abstract class Builder {
    private final Person person;

    protected Builder() {
        this.person = new Person();
        this.person.setPhoto(null);
        this.person.setDescription(null);
        this.person.setTheme(Theme.LIGHT);
    }

    public abstract void build();

    public void buildRole(Role role) {
        this.person.setRole(role);
    }

    public void buildUserName(String userName) {
        this.person.setUsername(userName);
    }


    public void buildName(String name) {
        this.person.setUsername(name);
    }

    public void buildPhoto(String photo) {
        this.person.setPhoto(photo);
    }

    public void buildEmail(String email) {
        this.person.setEmail(email);
    }

    public void buildGender(Gender gender) {
        this.person.setGender(gender);
    }

    public Person getResult() {
        return this.person;
    }

    public void buildDescription(String description) {
        this.person.setDescription(description);
    }

    public void buildTheme(Theme theme) {
        this.person.setTheme(theme);
    }

    public void updateAdmin(DatabaseService databaseService) {

        if (this.person.getRole() != Role.ADMIN)
            throw new PersonException("this Person is not an admin.");
        AdminEntity admin = new AdminEntity();
        admin.setUserName(this.person.getUsername());
        admin.setTheme(this.person.getTheme());
        databaseService.saveOrUpdate(admin);

    }

    public void updateUser(DatabaseService databaseService) {
        if (this.person.getRole() != Role.USER)
            throw new PersonException("this Person is not a user.");

        UserEntity user = new UserEntity();
        user.setUserName(this.person.getUsername());
        user.setGender(this.person.getGender());
        user.setEmail(this.person.getEmail());
        user.setPhoto(this.person.getPhoto());
        user.setDescription(this.person.getDescription());
        user.setTheme(this.person.getTheme());
        databaseService.saveOrUpdate(user);

    }

}

class PersonException extends RuntimeException {

    public PersonException(String message) {
        super(message);
    }

}