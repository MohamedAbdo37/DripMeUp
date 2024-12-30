package edu.alexu.cse.dripmeup.service.notifications;

import lombok.Getter;
import lombok.Setter;
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
@Setter
@Getter
public abstract class NotificationService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceLoader resourceLoader;

    // shared attributes between all classes

    // email and user_name of admin that we will send the email to
    private String email;
    private String username;
    private String filePath; // file path of email template
    private String subject; // subject of email
    private String body;


    // shared methods between all classes

    // Reading file from resource
    // throws exception if there is an error with file
    String readFileFromResources() throws IOException {
        Resource resource = this.resourceLoader.getResource(this.getFilePath());
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

    // opening connection and sending messages
    // throws exception if there is an error
    public boolean sendMessage() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(this.getEmail());
            message.setSubject(this.getSubject());
            message.setText(this.getBody());
            System.out.println(this.getBody());
            this.mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
