package edu.alexu.cse.dripmeup.Service.notifications;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AccountManagement extends NotificationService {

    // specific attribute for this class
    private int code ;
    public int getCode() {
        return this.code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    // IF there is an error with reading file return error else try to send message if there is an error return error
    // else return that email has been sent
    private String ManagingAccountMessage() {

        try {
            this.setBody(this.readFileFromResources()) ;
        }
        catch(IOException e) {
            return "Error occurred while reading file.";
        }

        // making message body
        this.setBody(this.getBody().replace("[User's Name]" , this.getUsername()));
        this.setBody(this.getBody().replace("[Code]" ,  String.valueOf(this.getCode())));
        if (this.sendMessage())
            return "email was sent" ;
        return "error in sending email" ;
    }

    // set body and subject of each message type
    public String VerifyAccount() {
        this.setSubject("Verify Your DripMeUp Store Account") ;
        this.setFilePath("file:src/main/resources/Notifications Body/AccountVerification.txt") ;
        return this.ManagingAccountMessage() ;
    }

    public String ChangeEmail() {
        this.setSubject ("Confirm Your Email Change Request") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/ChangingEmail.txt") ;
        return this.ManagingAccountMessage() ;
    }

    public String ForgetPassword(){
        this.setSubject("Password Change Verification Code") ;
        this.setFilePath("file:src/main/resources/Notifications Body/ForgetPassword.txt") ;
        return this.ManagingAccountMessage() ;
    }

}
