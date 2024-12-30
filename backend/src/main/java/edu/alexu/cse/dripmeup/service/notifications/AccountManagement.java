package edu.alexu.cse.dripmeup.service.notifications;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;

import java.io.IOException;


@Service
@Setter
@Getter
public class AccountManagement extends NotificationService {

    // specific attribute for this class
    private int code ;

    // IF there is an error with reading file return error else try to send message if there is an error return error
    // else return that email has been sent
    private String ManagingAccountMessage() throws IOException, FailedToSendMailException {

        this.setBody(this.readFileFromResources()) ;
    

        // making message body
        this.setBody(this.getBody().replace("[User's Name]" , this.getUsername()));
        this.setBody(this.getBody().replace("[Code]" ,  String.valueOf(this.getCode())));
        if (this.sendMessage())
            return "email was sent" ;
        else
            throw new FailedToSendMailException("error in sending email") ;
    }

    // set body and subject of each message type
    public String VerifyAccount() throws IOException, FailedToSendMailException {
        this.setSubject("Verify Your DripMeUp Store Account") ;
        this.setFilePath("file:src/main/resources/Notifications Body/AccountVerification.txt") ;
        return this.ManagingAccountMessage() ;
    }

    public String ChangeEmail() throws IOException, FailedToSendMailException {
        this.setSubject ("Confirm Your Email Change Request") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/ChangingEmail.txt") ;
        return this.ManagingAccountMessage() ;
    }

    public String ForgetPassword() throws IOException, FailedToSendMailException {
        this.setSubject("Password Change Verification Code") ;
        this.setFilePath("file:src/main/resources/Notifications Body/ForgetPassword.txt") ;
        return this.ManagingAccountMessage() ;
    }

}
