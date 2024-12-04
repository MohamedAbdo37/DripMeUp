package edu.alexu.cse.dripmeup.Service.notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public abstract class NotificationService {

    @Autowired
    private JavaMailSender mailsender ;
    @Autowired
    private ResourceLoader resourceLoader;

    private String email ;
    private String username ;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Reading file from resource
    String readFileFromResources(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource(filePath);
        InputStream inputStream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        reader.close();
        return stringBuilder.toString();
    }

    // sending message
    public boolean sendMessage(String body , String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(this.getEmail());
            message.setSubject(subject);
            message.setText(body);
            mailsender.send(message);
            return true ;
        }
        catch (Exception e){
            System.out.println(e);
            return false ;
        }
    }

}
