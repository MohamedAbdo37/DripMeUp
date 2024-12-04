package edu.alexu.cse.dripmeup.Entity;

import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Enumeration.Role;
import edu.alexu.cse.dripmeup.Enumeration.Theme;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.DatabaseService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Setter
@Getter
public class Person {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    private Role role;
    private int personID;
    private String username;
    private String name;
    private String photo;
//    private List<> favorites;
    private String description;
    private String email;
    private String phone;
//    private Address address;
    private Gender gender;
    private Theme theme;
    private Date Bdate;

    public void updatePassword(String password){
        if(this.getRole() == Role.ADMIN) {
            AdminEntity admin = adminRepository.findByUserName(this.getUsername()).getFirst();
            admin.setPassword(password);
            databaseService.saveOrUpdate(admin);
        }
        if(this.getRole() == Role.USER) {
            UserEntity user = userRepository.findByEmail(this.getEmail()).getFirst();
            user.setPassword(password);
            databaseService.saveOrUpdate(user);
        }
    }

    public EntityIF getTuble(){
        if (this.getRole() == Role.ADMIN)
            return adminRepository.findByUserName(this.getUsername()).getFirst();
        if (this.getRole() == Role.USER)
            return userRepository.findByEmail(this.getEmail()).getFirst();
        else
            return null;
    }

    public EntityIF toEntity(){
        if (this.getRole() == Role.ADMIN) {
            if (adminRepository.findByUserName(this.getUsername()).isEmpty())
                return new AdminEntity(this.getUsername(), this.getPhoto(), "", this.gender);
            else
                return this.getTuble();
        }else if (this.getRole() == Role.USER) {
                if (adminRepository.findByUserName(this.getUsername()).isEmpty())
                    return new AdminEntity(this.getUsername(), this.getPhoto(), "", this.gender);
                else
                    return this.getTuble();
        }

        return null;
    }
}
