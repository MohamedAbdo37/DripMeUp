package edu.alexu.cse.dripmeup.Service.Handler;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Excpetion.HandlerException;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;

public class CreatorIsAdminHandler extends Handler {
    private final Person person;
    private final AdminEntity admin;
    private final AdminRepository adminRepository;

    public CreatorIsAdminHandler(Person person, AdminEntity admin, AdminRepository adminRepository1) {
        this.person = person;
        this.admin = admin;
        this.adminRepository = adminRepository1;
    }

    @Override
    public void handle() {
        if (person == null || admin == null) {
            throw new HandlerException("Invalid input.");
        }

        if (person.getRole() != Role.ADMIN) {
            throw new HandlerException(
                    "User " + this.person.getUsername() + " can't create admin account.");
        }

        this.handleNext();
    }

    @Override
    public void handleNext() {
        new ValidAdminUserNameHandler(this.admin.getUserName(), this.adminRepository).handle();
    }

}
