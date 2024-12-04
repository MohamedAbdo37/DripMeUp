package edu.alexu.cse.dripmeup;
import edu.alexu.cse.dripmeup.Service.notifications.AccountManagement;
import edu.alexu.cse.dripmeup.Service.notifications.OrderManagement;
import edu.alexu.cse.dripmeup.Service.notifications.WelcomeandGoodbyeManagement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    AccountManagement accountManagement;
    @Autowired
    WelcomeandGoodbyeManagement welcomeandGoodbyeManagement ;

    @Autowired
    OrderManagement orderManagement ;


    @Test
    void testVerifyAccount_Success() {
        accountManagement.setEmail("ni254828@gmail.com");
        accountManagement.setCode(123456);
        accountManagement.setUsername("Nira Ibrahim");
        String result = accountManagement.VerifyAccount();
        assertEquals(result , "email was sent");
    }

    @Test
    void testChangeEmail_Success(){
        accountManagement.setEmail("ni254828@gmail.com");
        accountManagement.setCode(123456);
        accountManagement.setUsername("Nira Ibrahim");
        String result = accountManagement.ChangeEmail();
        assertEquals(result , "email was sent");
    }

    @Test
    void testForgetPassword_Success(){
        accountManagement.setEmail("ni254828@gmail.com");
        accountManagement.setCode(123456);
        accountManagement.setUsername("Nira Ibrahim");
        String result = accountManagement.ForgetPassword();
        assertEquals(result , "email was sent");
    }

    @Test
    void testSendOrder_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.SendOrder();
        assertEquals(result , "email was sent");
    }

    @Test
    void testCancelOrder_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.CancelOrder();
        assertEquals(result , "email was sent");
    }

    @Test
    void testConfirmOrder_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.ConfirmOrder();
        assertEquals(result , "email was sent");
    }

    @Test
    void testReceiveOrder_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.ReceiveOrder();
        assertEquals(result , "email was sent");
    }

    @Test
    void testShipOrder_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.ShipOrder();
        assertEquals(result , "email was sent");
    }

    @Test
    void testInformlOrderError_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.InformOrderError();
        assertEquals(result , "email was sent");
    }

    @Test
    void testInformShippingProblem_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.InformShippingProblem();
        assertEquals(result , "email was sent");
    }

    @Test
    void testInformReceivingProblem_Success(){
        orderManagement.setEmail("ni254828@gmail.com");
        orderManagement.setOrderId(1000);
        orderManagement.setUsername("Nira Ibrahim");
        String result = orderManagement.InformReceivingProblem();
        assertEquals(result , "email was sent");
    }


    @Test
    void testWelcomeMessage_Success() {
        welcomeandGoodbyeManagement.setEmail("ni254828@gmail.com");
        welcomeandGoodbyeManagement.setUsername("Nira Ibrahim");
        String result = welcomeandGoodbyeManagement.WelcomeMessage();
        assertEquals(result , "email was sent");
    }

    @Test
    void testVerifyGoodbyeMessage() {
        welcomeandGoodbyeManagement.setEmail("ni254828@gmail.com");
        welcomeandGoodbyeManagement.setUsername("Nira Ibrahim");
        String result = welcomeandGoodbyeManagement.GoodbyeMessage();
        assertEquals(result , "email was sent");
    }

//    @Test
//    void testAccountManagementFileError() throws IOException {
//        Path source = Paths.get("src/main/resources/Notifications Body/AccountVerification.txt") ;
//        Path target = Paths.get("src/main/resources/AccountVerification.txt") ;
//        Files.move(source , target) ;
//        accountManagement.setEmail("ni254828@gmail.com");
//        accountManagement.setCode(123456);
//        accountManagement.setUsername("Nira Ibrahim");
//        String result = accountManagement.VerifyAccount();
//        assertEquals(result , "Error occurred while reading file.");
//    }
//
//    @Test
//    void testWelcoemManagementFileError() throws IOException{
//        Path source = Paths.get("src/main/resources/Notifications Body/WelcomeMessage.txt") ;
//        Path target = Paths.get("src/main/resources/WelcomeMessage.txt") ;
//        Files.move(source , target) ;
//        welcomeandGoodbyeManagement.setEmail("ni254828@gmail.com");
//        welcomeandGoodbyeManagement.setUsername("Nira Ibrahim");
//        String result = welcomeandGoodbyeManagement.WelcomeMessage();
//        assertEquals(result , "Error occurred while reading file.");
//    }
//
//    @Test
//    void testOrderManagementFileError() throws IOException{
//        Path source = Paths.get("src/main/resources/Notifications Body/MakingOrderByCustomer.txt") ;
//        Path target = Paths.get("src/main/resources/MakingOrderByCustomer.txt") ;
//        Files.move(source , target) ;
//        orderManagement.setEmail("ni254828@gmail.com");
//        orderManagement.setOrderId(1000);
//        orderManagement.setUsername("Nira Ibrahim");
//        String result = orderManagement.SendOrder();
//        assertEquals(result , "Error occurred while reading file.");
//    }


    //    @Test
//    void testAccountManagementSendingError(){
//        Path source = Paths.get("src/main/resources/Notifications Body/AccountVerification.txt") ;
//        Path target = Paths.get("src/main/resources/AccountVerification.txt") ;
//        Files.move(source , target) ;
//        accountManagement.setEmail("ni254828@gmail.com");
//        accountManagement.setCode(123456);
//        accountManagement.setUsername("Nira Ibrahim");
//        String result = accountManagement.VerifyAccount();
//        assertEquals(result , "error in sending email");
//    }
//
//    @Test
//    void testWelcoemManagementSendingError() throws IOException{
//        Path source = Paths.get("src/main/resources/Notifications Body/WelcomeMessage.txt") ;
//        Path target = Paths.get("src/main/resources/WelcomeMessage.txt") ;
//        Files.move(source , target) ;
//        welcomeandGoodbyeManagement.setEmail("ni254828@gmail.com");
//        welcomeandGoodbyeManagement.setUsername("Nira Ibrahim");
//        String result = welcomeandGoodbyeManagement.WelcomeMessage();
//        assertEquals(result , "error in sending email");
//    }
//
//    @Test
//    void testOrderManagementSendingError() throws IOException{
//        Path source = Paths.get("src/main/resources/Notifications Body/MakingOrderByCustomer.txt") ;
//        Path target = Paths.get("src/main/resources/MakingOrderByCustomer.txt") ;
//        Files.move(source , target) ;
//        orderManagement.setEmail("ni254828@gmail.com");
//        orderManagement.setOrderId(1000);
//        orderManagement.setUsername("Nira Ibrahim");
//        String result = orderManagement.SendOrder();
//        assertEquals(result , "error in sending email");
//    }
}
