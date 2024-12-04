package edu.alexu.cse.dripmeup.Service.notifications;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class WelcomeandGoodbyeManagement extends NotificationService{

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

    private String WelcomeandGoodbyeManagementMessage() {

        String body = "" ;
        try {
            body = this.readFileFromResources(this.getFilePath()) ;
        }
        catch(IOException e) {
            return "Error occurred while reading file.";
        }

        // making message body
        body = body.replace("[User\'s Name]" , this.getUsername()) ;
        if (this.sendMessage(body , this.getSubject()))
            return "email was sent" ;
        return "error in sending email" ;
    }

    public String WelcomeMessage() {
        this.setSubject("Thank You for Joining DripMeUp Store") ;
        this.setFilePath("file:src/main/resources/Notifications Body/WelcomeMessage.txt") ;
        return this.WelcomeandGoodbyeManagementMessage() ;
    }

    public String GoodbyeMessage() {
        this.setSubject ("Account Deletion Successful") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/DeletingAccount.txt") ;
        return this.WelcomeandGoodbyeManagementMessage() ;
    }
}
