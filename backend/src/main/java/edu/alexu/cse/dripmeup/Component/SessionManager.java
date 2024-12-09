package edu.alexu.cse.dripmeup.Component;

import java.util.Base64;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.AdminService;
import edu.alexu.cse.dripmeup.Service.PersonDirector;
import edu.alexu.cse.dripmeup.Service.UserService;
import edu.alexu.cse.dripmeup.Service.builder.AdminPersonBuilder;
import edu.alexu.cse.dripmeup.Service.builder.UserPersonBuilder;
import edu.alexu.cse.dripmeup.excpetion.AuthorizationException;
import edu.alexu.cse.dripmeup.excpetion.HandlerException;

@Component
public class SessionManager {

    private AdminRepository adminRepository = null;
    private UserRepository userRepository = null;

    public SessionManager(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public SessionManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Person adminSignUP(String userName, String password) {
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(password);
        AdminService service = new AdminService(adminRepository);
        return service.createAdmin(admin);
    }

    public Person adminLogin(String userName, String password) {
        boolean isAuthenticated = new AdminService(adminRepository).adminLogin(userName, password);

        if (isAuthenticated){
            AdminEntity admin = adminRepository.findByUserName(userName);
            return new PersonDirector().construct(new AdminPersonBuilder(admin, adminRepository));
        }
        
        return null;
    }

    public Person userLogin(String email, String password) {
        boolean isAuthenticated = new UserService().login(email, password);
        if (isAuthenticated){
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        }
        return null;
    }

    public Person userLogin(String token) throws AuthorizationException{
        String email = this.extractEmail(token);
        boolean isAuthenticated = new UserService().logInWithoutPassword(email);
        if (isAuthenticated){
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        }
        return null;
    }

    public Person userSignUp(UserEntity newUser) throws HandlerException {
        return new UserService().signup(newUser);
    }

    public Person userSignUp(UserEntity user, String token) throws AuthorizationException, HandlerException {
        String email = this.extractEmail(token);

        if(!email.equals(user.getEmail())) 
            throw new AuthorizationException("Emails do not match");

        return new UserService().signup(user);
    }

    private String extractEmail(String token){
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(token));
        JSONObject jsonObject = new JSONObject(payload);
        return jsonObject.getString("email");
    }
}

