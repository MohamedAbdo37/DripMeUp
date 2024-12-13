package edu.alexu.cse.dripmeup.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Base64;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import edu.alexu.cse.dripmeup.Entity.CodeEntity;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Excpetion.AuthorizationException;
import edu.alexu.cse.dripmeup.Excpetion.HandlerException;
import edu.alexu.cse.dripmeup.Excpetion.SendMailException;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Repository.CodeRepository;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.AdminService;
import edu.alexu.cse.dripmeup.Service.Builder.AdminPersonBuilder;
import edu.alexu.cse.dripmeup.Service.Builder.UserPersonBuilder;
import edu.alexu.cse.dripmeup.Service.notifications.AccountManagement;
import edu.alexu.cse.dripmeup.Service.PersonDirector;
import edu.alexu.cse.dripmeup.Service.UserService;

@Component
public class SessionManager {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private AccountManagement accountManagement;

    private final Random random = new Random();

    public Person adminSignUP(String userName, String password) {
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(password);
        AdminService service = new AdminService(adminRepository);
        return service.createAdmin(admin);
    }

    public Person adminLogin(String userName, String password) {
        boolean isAuthenticated = new AdminService(adminRepository).adminLogin(userName, password);

        if (isAuthenticated) {
            AdminEntity admin = adminRepository.findByUserName(userName);
            return new PersonDirector().construct(new AdminPersonBuilder(admin, adminRepository));
        }

        return null;
    }

    public Person userLogin(String email, String password) {
        boolean isAuthenticated = new UserService(this.userRepository).login(email, password);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        }
        return null;
    }

    public Person userLogin(String token) throws AuthorizationException {
        String email = this.extractEmail(token);
        boolean isAuthenticated = new UserService(this.userRepository).isEmailPresent(email);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        } else
            throw new AuthorizationException("Not Authorised");

    }

    public Person userSignUp(UserEntity newUser) throws HandlerException {
        return new UserService(this.userRepository).signup(newUser);
    }

    public Person userSignUp(UserEntity user, String token) throws AuthorizationException, HandlerException {
        String email = this.extractEmail(token);

        if (!email.equals(user.getEmail()))
            throw new AuthorizationException("Emails do not match");

        return new UserService(this.userRepository).signup(user);
    }

    private String extractEmail(String token) {
        String[] parts = token.split("\\.", 0);
        byte[] bytes = Base64.getUrlDecoder().decode(parts[1]);
        String decodedString = new String(bytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(decodedString);

        return jsonObject.getString("email");
    }

    public AdminRepository getAdminRepository() {
        return adminRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public boolean changePassword(String email, String password) throws AuthorizationException {
        UserEntity newPassword = new UserEntity();
        newPassword.setPassword(password);
        boolean isAuthenticated = new UserService(this.userRepository).isEmailPresent(email);
        if (isAuthenticated) {
            return new UserService(this.userRepository).changePassword(email, newPassword);
        } else
            throw new AuthorizationException("Not Authorized");
    }

    public Person forgetPasswordPerson(String email) throws AuthorizationException {
        boolean isAuthenticated = new UserService(this.userRepository).isEmailPresent(email);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        } else
            throw new AuthorizationException("Not Authorized");
    }

    public String generateCodeSignUp(String email, String userName) throws IOException, SendMailException {
        int code = this.random.nextInt(100000, 1000000);
        this.accountManagement.setEmail(email);
        this.accountManagement.setUsername(userName);
        this.accountManagement.setCode(code);

        System.out.println(email + ", " + code + ", message : " + this.accountManagement.VerifyAccount());

        CodeEntity codeEntity = new CodeEntity();
        codeEntity.setCode(code);
        codeEntity.setEmail(email);
        codeRepository.save(codeEntity);

        return String.valueOf(codeEntity.getID());
    }

    public String generateCodeForgetPassword(String email, String userName) throws IOException, SendMailException {
        int code = this.random.nextInt(100000, 1000000);

        this.accountManagement.setEmail(email);
        this.accountManagement.setUsername(userName);
        this.accountManagement.setCode(code);

        System.out.println(email + ", " + code + ", message : " + this.accountManagement.ForgetPassword());

        CodeEntity codeEntity = new CodeEntity();
        codeEntity.setCode(code);
        codeEntity.setEmail(email);
        codeRepository.save(codeEntity);

        return String.valueOf(codeEntity.getID());
    }

    public boolean checkCode(String codeID, String code) {
        Long codeIDLong = Long.valueOf(codeID);
        System.out.println("codeID: " + codeID);
        CodeEntity codeEntity = codeRepository.getReferenceById(codeIDLong);
        System.out.println("codeEntity" + codeEntity.toString());
        if (!Integer.valueOf(code).equals(codeEntity.getCode()))
            return false;

        this.codeRepository.delete(codeEntity);
        return true;
    }
}
