package edu.alexu.cse.dripmeup.Service.notifications;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AccountManagement extends NotificationService {

    private String filePath ;
    private String subject ;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getSubject() {
        return this.subject;
    }

    private int code ;
    public int getCode() {
        return this.code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    private String ManagingAccountMessage() {

        String body = "" ;
        try {
            body = this.readFileFromResources(this.getFilePath()) ;
        }
        catch(IOException e) {
            return "Error occurred while reading file.";
        }

        // making message body
        body = body.replace("[User\'s Name]" , this.getUsername()) ;
        body = body.replace("[Code]" ,  String.valueOf(this.getCode())) ;
        if (this.sendMessage(body , this.getSubject()))
            return "email was sent" ;
        return "error in sending email" ;
    }

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
