package edu.alexu.cse.dripmeup.service.notifications;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;

@Service
public class WelcomeGoodbyeManagement extends NotificationService{

    // IF there is an error with reading file return error else try to send message if there is an error return error
    // else return that email has been sent
    private String WelcomeGoodbyeManagementMessage() throws IOException, FailedToSendMailException {


        this.setBody(this.readFileFromResources()) ;

        // making message body
        this.setBody(this.getBody().replace("[User's Name]" , this.getUsername()));
        if (this.sendMessage())
            return "email was sent" ;
            
        throw new FailedToSendMailException("error in sending email");
    }

    // set body and subject of each message type
    public String WelcomeMessage() throws IOException, FailedToSendMailException {
        this.setResource(new ClassPathResource("NotificationsBody/WelcomeMessage.txt"));
        this.setSubject("Thank You for Joining DripMeUp Store") ;
        return this.WelcomeGoodbyeManagementMessage() ;
    }

    public String GoodbyeMessage() throws IOException, FailedToSendMailException {
        this.setResource(new ClassPathResource("NotificationsBody/DeletingAccount.txt"));
        this.setSubject ("Account Deletion Successful") ;
        return this.WelcomeGoodbyeManagementMessage() ;
    }
}
