package edu.alexu.cse.dripmeup.Service;

import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Service.Handler.CreatorIsAdminHandler;
import edu.alexu.cse.dripmeup.Service.Handler.HandlerException;
import edu.alexu.cse.dripmeup.Service.builder.AdminPersonBuilder;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    // @Contract(pure = true)
    public Person createAdmin(Person admin, AdminEntity newAdmin){

        try {
            new CreatorIsAdminHandler(admin, newAdmin, adminRepository).handle();
        } catch (HandlerException e) {
            return null;
        }

        // create new person
        return new PersonDirector().construct(new AdminPersonBuilder(newAdmin,this.adminRepository));
    }
}