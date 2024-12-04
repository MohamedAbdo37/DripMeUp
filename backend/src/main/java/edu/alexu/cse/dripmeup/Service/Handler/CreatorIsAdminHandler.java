package edu.alexu.cse.dripmeup.Service.Handler;


import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Enumeration.Role;


public class CreatorIsAdminHandler extends Handler {
    private final Person person;
    private final String userName;
    public CreatorIsAdminHandler(Person person, String userName ){
        this.person = person;
        this.userName = userName;
        this.handle();
        this.handleNext();
    }

    @Override
    public void handle() {
        if (person != null){
            if( person.getRole() != Role.ADMIN) {
                throw new HandlerException(
                        "CreatorIsAdminHandler:User " + this.person.getUsername() +" can't create admin account.");
            }
        }else {
            throw new HandlerException("CreatorIsAdminHandler:Invalid input.");
        }

    }

    @Override
    public void handleNext() {
        new ValidAdminUserNameHandler(this.userName);
    }

}
