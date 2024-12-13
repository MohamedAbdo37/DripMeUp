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

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // setters & getters
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

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
