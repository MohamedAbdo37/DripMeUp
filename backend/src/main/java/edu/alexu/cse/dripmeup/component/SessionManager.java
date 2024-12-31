package edu.alexu.cse.dripmeup.component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.entity.CodeEntity;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.exception.AuthorizationException;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.exception.InvalidResendCodeException;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import edu.alexu.cse.dripmeup.repository.AdminRepository;
import edu.alexu.cse.dripmeup.repository.CodeRepository;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.service.AdminService;
import edu.alexu.cse.dripmeup.service.builder.AdminPersonBuilder;
import edu.alexu.cse.dripmeup.service.builder.UserPersonBuilder;
import edu.alexu.cse.dripmeup.service.PersonDirector;
import edu.alexu.cse.dripmeup.service.UserService;
import edu.alexu.cse.dripmeup.service.notifications.AccountManagement;
import edu.alexu.cse.dripmeup.service.notifications.WelcomeGoodbyeManagement;

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

    @Autowired
    private WelcomeGoodbyeManagement welcomeGoodbyeManagement;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    private final Random random = new Random();
    private final int expiryTime = 1;

    public Person adminSignUP(String userName, String password) {
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(password);
        return adminService.createAdmin(admin);
    }

    public Person adminLogin(String userName, String password) {
        boolean isAuthenticated = adminService.adminLogin(userName, password);

        if (isAuthenticated) {
            AdminEntity admin = adminRepository.findByUserName(userName);
            return new PersonDirector().construct(new AdminPersonBuilder(admin, adminRepository));
        }

        return null;
    }

    public Person userLogin(String email, String password) {
        boolean isAuthenticated = userService.login(email, password);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        }
        return null;
    }

    public Person userLogin(String token) throws AuthorizationException {
        String email = this.extractEmail(token);
        boolean isAuthenticated = userService.isEmailPresent(email);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        } else
            throw new AuthorizationException("Not Authorized");

    }

    public Person userSignUp(UserEntity newUser) throws HandlerException {
        return userService.signup(newUser);
    }

    public Person userSignUp(UserEntity user, String token) throws AuthorizationException, HandlerException {
        String email = this.extractEmail(token);

        if (!email.equals(user.getEmail()))
            throw new AuthorizationException("Emails do not match");

        return userService.signup(user);
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
        boolean isAuthenticated = userService.isEmailPresent(email);
        if (isAuthenticated) {
            return userService.changePassword(email, newPassword);
        } else
            throw new AuthorizationException("Not Authorized");
    }

    public Person forgetPasswordPerson(String email) throws AuthorizationException {
        boolean isAuthenticated =userService.isEmailPresent(email);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            return new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
        } else
            throw new AuthorizationException("Not Authorized");
    }

    public String generateCodeSignUp(String email, String userName) throws IOException, FailedToSendMailException, InvalidResendCodeException {
        int code = this.random.nextInt(100000, 1000000);
        System.out.println(code);
        this.accountManagement.setEmail(email);
        this.accountManagement.setUsername(userName);
        this.accountManagement.setCode(code);

        System.out.println(email + ", " + code + ", message : " + this.accountManagement.VerifyAccount());
        
        CodeEntity codeEntity = this.codeRepository.findByEmail(email);
        if (codeEntity != null){
            if(codeEntity.getExpiredDate().isAfter(LocalDateTime.now()))
                throw new InvalidResendCodeException("Code has already been sent");
            else
                this.codeRepository.delete(codeEntity);
        }
        
        codeEntity = new CodeEntity();
        codeEntity.setCode(code);
        codeEntity.setEmail(email);
        codeEntity.setExpiredDate(LocalDateTime.now().plusMinutes(this.expiryTime));
        codeRepository.save(codeEntity);

        return String.valueOf(codeEntity.getCodeID());
    }

    public String generateCodeForgetPassword(String email, String userName) throws IOException, FailedToSendMailException, InvalidResendCodeException {
        int code = this.random.nextInt(100000, 1000000);

        this.accountManagement.setEmail(email);
        this.accountManagement.setUsername(userName);
        this.accountManagement.setCode(code);

        System.out.println("ForgetPassword");
        System.out.println(email + ", " + code + ", message : " + this.accountManagement.ForgetPassword());

        CodeEntity codeEntity = this.codeRepository.findByEmail(email);
        if (codeEntity != null){
            if(codeEntity.getExpiredDate().isAfter(LocalDateTime.now()))
                throw new InvalidResendCodeException("Code has already been sent");
            else
                this.codeRepository.delete(codeEntity);
        }

        codeEntity = new CodeEntity();
        codeEntity.setCode(code);
        codeEntity.setEmail(email);
        codeEntity.setCreatedDate(LocalDateTime.now());
        codeEntity.setExpiredDate(LocalDateTime.now().plusMinutes(this.expiryTime));
        codeRepository.save(codeEntity);

        return String.valueOf(codeEntity.getCodeID());
    }
    
    public void sendGreeting(String email, String userName) throws IOException, FailedToSendMailException {
        this.welcomeGoodbyeManagement.setEmail(email);
        this.welcomeGoodbyeManagement.setUsername(userName);
        System.out.println("Greeting");
        System.out.print(email + ", message : ");
        System.out.println(this.welcomeGoodbyeManagement.WelcomeMessage());
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
