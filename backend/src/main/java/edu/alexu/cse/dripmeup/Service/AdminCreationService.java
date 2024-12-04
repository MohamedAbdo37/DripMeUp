package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Service.Builder.AdminPersonBuilder;
import edu.alexu.cse.dripmeup.Service.Handler.CreatorIsAdminHandler;
import edu.alexu.cse.dripmeup.Service.Handler.HandlerException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class AdminCreationService {

    @Contract(pure = true)
    public static @NotNull Person createAdmin(Person admin, String userName, String password, Gender gender){

        try {
            new CreatorIsAdminHandler(admin, userName).handle();
        } catch (HandlerException e) {
            //noinspection DataFlowIssue
            return null;
        }

        // create new person
        return new PersonDirector().construct(new AdminPersonBuilder(userName,password,gender));
    }
}
