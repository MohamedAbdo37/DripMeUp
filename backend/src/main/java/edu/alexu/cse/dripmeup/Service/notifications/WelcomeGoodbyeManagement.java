package edu.alexu.cse.dripmeup.Service.notifications;
import java.io.IOException;

import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.Excpetion.SendMailException;

@Service
public class WelcomeGoodbyeManagement extends NotificationService{

    // IF there is an error with reading file return error else try to send message if there is an error return error
    // else return that email has been sent
    private String WelcomeGoodbyeManagementMessage() throws IOException, SendMailException {


        this.setBody(this.readFileFromResources()) ;

        // making message body
        this.setBody(this.getBody().replace("[User's Name]" , this.getUsername()));
        if (this.sendMessage())
            return "email was sent" ;
            
        throw new SendMailException("error in sending email");
    }

    // set body and subject of each message type
    public String WelcomeMessage() throws IOException, SendMailException {
        this.setSubject("Thank You for Joining DripMeUp Store") ;
        this.setFilePath("file:src/main/resources/Notifications Body/WelcomeMessage.txt") ;
        return this.WelcomeGoodbyeManagementMessage() ;
    }

    public String GoodbyeMessage() throws IOException, SendMailException {
        this.setSubject ("Account Deletion Successful") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/DeletingAccount.txt") ;
        return this.WelcomeGoodbyeManagementMessage() ;
    }
}
