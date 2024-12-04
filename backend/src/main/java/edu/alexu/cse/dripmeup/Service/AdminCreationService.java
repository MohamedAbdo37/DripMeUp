package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Builder.AdminPersonBuilder;
import edu.alexu.cse.dripmeup.Service.Handler.CreatorIsAdminHandler;
import edu.alexu.cse.dripmeup.Service.Handler.HandlerException;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCreationService {

    @Autowired
    private static AdminRepository adminRepository;

    @Contract(pure = true)
    public static Person createAdmin(Person admin, AdminEntity newAdmin){

        try {
            new CreatorIsAdminHandler(admin, newAdmin, adminRepository).handle();
        } catch (HandlerException e) {
            return null;
        }

        // create new person
        return new PersonDirector().construct(new AdminPersonBuilder(newAdmin));
    }
}
