package edu.alexu.cse.dripmeup;
import edu.alexu.cse.dripmeup.dto.OrderDTO;
import edu.alexu.cse.dripmeup.dto.OrderMetaDTO;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import edu.alexu.cse.dripmeup.service.notifications.AccountManagement;
import edu.alexu.cse.dripmeup.service.notifications.OrderManagement;
import edu.alexu.cse.dripmeup.service.notifications.WelcomeGoodbyeManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationsTest {

    @InjectMocks
    @Autowired
    AccountManagement accountManagement ;

    @InjectMocks
    @Autowired
    OrderManagement orderManagement ;
    @InjectMocks
    @Autowired
    WelcomeGoodbyeManagement welcomeGoodbyeManagement ;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testVerifyAccount_Success() throws IOException , FailedToSendMailException{

        // Arrange
        accountManagement.setEmail("ni254828@gmail.com");
        accountManagement.setCode(123456);
        accountManagement.setUsername("Nira Ibrahim");

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = accountManagement.VerifyAccount();

        // Assert
        assertEquals(result, "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testChangeEmail_Success() throws IOException , FailedToSendMailException{

        // Arrange
        accountManagement.setEmail("ni254828@gmail.com");
        accountManagement.setCode(123456);
        accountManagement.setUsername("Nira Ibrahim");

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = accountManagement.ChangeEmail();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once

    }


    @Test
    void testForgetPassword_Success() throws IOException , FailedToSendMailException{

        // Arrange
        accountManagement.setEmail("ni254828@gmail.com");
        accountManagement.setCode(123456);
        accountManagement.setUsername("Nira Ibrahim");

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = accountManagement.ForgetPassword();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testGoodbyeMessage_Success() throws IOException , FailedToSendMailException {

        //Arrange
        welcomeGoodbyeManagement.setEmail("ni254828@gmail.com");
        welcomeGoodbyeManagement.setUsername("Nira Ibrahim");

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = welcomeGoodbyeManagement.GoodbyeMessage();

        //Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once

    }

    @Test
    void testWelcomeMessage_Success() throws IOException , FailedToSendMailException {

        // Arrange
        welcomeGoodbyeManagement.setEmail("ni254828@gmail.com");
        welcomeGoodbyeManagement.setUsername("Naira Ibrahim");

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = welcomeGoodbyeManagement.WelcomeMessage() ;

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once

    }

    @Test
    void testSendOrder_Success() throws FailedToSendMailException{

        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));
        // Act
        String result = orderManagement.SendOrder();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testCancelOrder_Success() throws FailedToSendMailException{

        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = orderManagement.CancelOrder();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testConfirmOrder_Success() throws FailedToSendMailException{

        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = orderManagement.ConfirmOrder();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testReceiveOrder_Success() throws FailedToSendMailException{

        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = orderManagement.ReceiveOrder();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testShipOrder_Success() throws FailedToSendMailException{
        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = orderManagement.ShipOrder();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testInformOrderError_Success() throws FailedToSendMailException{

        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = orderManagement.InformOrderError();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testInformShippingProblem_Success() throws FailedToSendMailException{

        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = orderManagement.InformShippingProblem();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }

    @Test
    void testInformReceivingProblem_Success() throws FailedToSendMailException{

        // Arrange
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = orderManagement.InformReceivingProblem();

        // Assert
        assertEquals(result , "email was sent");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
    }


    @Test
    void testVerifyAccount_Failure() {

        try {
            // Arrange
            accountManagement.setEmail("ni254828@gmail.com");
            accountManagement.setCode(123456);
            accountManagement.setUsername("Nira Ibrahim");

            // Mock JavaMailSender to throw an exception
            doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));
            // Act
            accountManagement.VerifyAccount();

            // Assert

        }
        catch (FailedToSendMailException | IOException e){
            assertEquals(e.getMessage(), "error in sending email");
            verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
        }

    }


    @Test
    void testWelcomeMessage_Failure() {

        try {
            // Arrange
            welcomeGoodbyeManagement.setEmail("ni254828@gmail.com");
            welcomeGoodbyeManagement.setUsername("Naira Ibrahim");

            // Mock JavaMailSender to throw an exception
            doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

            // Act
            welcomeGoodbyeManagement.WelcomeMessage();

        }
        catch (FailedToSendMailException | IOException e){
            assertEquals(e.getMessage(), "error in sending email");
            verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
        }
    }


    @Test
    void testSendOrder_Failure(){
        try {
            // Arrange
            orderManagement.setEmail("ni254828@gmail.com");
            orderManagement.setOrderId(1000);
            orderManagement.setUsername("Nira Ibrahim");

            orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

            // Mock JavaMailSender to throw an exception
            doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

            // Act
            orderManagement.SendOrder();

        }
        catch (FailedToSendMailException e){
            assertEquals(e.getMessage(), "Failed to send email");
            verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify send() was called once
        }
    }

    @Test
    void testAccountManagement_FileError() throws IOException {
        // Arrange
        // moving file to different place to give error
        Path source = Paths.get("src/main/resources/Notifications Body/AccountVerification.txt");
        Path target = Paths.get("src/main/resources/AccountVerification.txt");
        try {
            Files.move(source, target);
            accountManagement.setEmail("ni254828@gmail.com");
            accountManagement.setCode(123456);
            accountManagement.setUsername("Nira Ibrahim");

            // Mock the behavior of JavaMailSender
            doNothing().when(mailSender).send(any(SimpleMailMessage.class));

            // Act
            accountManagement.VerifyAccount();

        }
        catch (FailedToSendMailException | IOException e){
            verify(mailSender, times(0)).send(any(SimpleMailMessage.class)); // Verify send() wasn't called
            Files.move(target, source);
        }
    }

    @Test
    void testWelcomeManagement_FileError() throws IOException {
        // Arrange
        // moving file to different place to give error
        Path source = Paths.get("src/main/resources/Notifications Body/WelcomeMessage.txt") ;
        Path target = Paths.get("src/main/resources/WelcomeMessage.txt") ;
        try {
            Files.move(source, target);
            welcomeGoodbyeManagement.setEmail("ni254828@gmail.com");
            welcomeGoodbyeManagement.setUsername("Nira Ibrahim");

            // Mock the behavior of JavaMailSender
            doNothing().when(mailSender).send(any(SimpleMailMessage.class));

            // Act
            welcomeGoodbyeManagement.WelcomeMessage();
        }
        catch (IOException | FailedToSendMailException e) {
            // Assert
            verify(mailSender, times(0)).send(any(SimpleMailMessage.class)); // Verify send() wasn't called
            // return file to its original place
            Files.move(target, source);
        }
    }



    @Test
    void testOrderManagement_FileError() throws IOException{
        // Arrange
        // moving file to different place to give error

        Path source = Paths.get("src/main/resources/Notifications Body/MakingOrderByCustomer.txt") ;
        Path target = Paths.get("src/main/resources/MakingOrderByCustomer.txt") ;
        Files.move(source , target) ;
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");

        orderManagement.setOrderDTO(new OrderDTO(new LinkedList<>() , new OrderMetaDTO()));

        // Mock the behavior of JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));


        // Act
        String result = orderManagement.SendOrder();

        // Assert
        assertEquals(result , "Error occurred while reading file.");
        verify(mailSender, times(0)).send(any(SimpleMailMessage.class)); // Verify send() wasn't called

        // return file to its original place
        Files.move(target , source) ;
    }
}

