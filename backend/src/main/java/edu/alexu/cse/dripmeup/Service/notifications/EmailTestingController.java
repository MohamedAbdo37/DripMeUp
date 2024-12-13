package edu.alexu.cse.dripmeup.Service.notifications;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.Excpetion.FailedToSendMailException;


@RestController
@RequestMapping("/DripMeUp")
public class EmailTestingController {
    @Autowired
    AccountManagement accountManagement ;

    @Autowired
    WelcomeGoodbyeManagement welcomeGoodbyeManagement;

    @Autowired
    OrderManagement orderManagement ;

    @GetMapping("/sending-email")
    public void send_email_test(){

        accountManagement.setEmail("ni254828@gmail.com");
        accountManagement.setUsername("Nira Ibrahim");
        accountManagement.setCode(123456);
        try{
            System.out.println(accountManagement.VerifyAccount());
            // System.out.println(accountManagement.ChangeEmail());
            // System.out.println(accountManagement.ForgetPassword());
        } catch (IOException | FailedToSendMailException e) {
            System.out.println(e.getMessage());
        }
//
//        welcomeGoodbyeManagement.setEmail("ni254828@gmail.com");
//        welcomeGoodbyeManagement.setUsername("Nira Ibrahim");
//        System.out.println(welcomeGoodbyeManagement.WelcomeMessage());
//        System.out.println(welcomeGoodbyeManagement.GoodbyeMessage());
//
//
//        orderManagement.setEmail("niraibrahem99@gmail.com");
//        orderManagement.setUsername("Nira Ibrahim");
//        orderManagement.setOrderId(1000);
//        System.out.println(orderManagement.SendOrder());
//        System.out.println(orderManagement.CancelOrder());
//        System.out.println(orderManagement.ConfirmOrder());
//        System.out.println(orderManagement.InformOrderError());
//        System.out.println(orderManagement.ShipOrder());
//        System.out.println(orderManagement.InformShippingProblem());
//        System.out.println(orderManagement.ReceiveOrder());
//        System.out.println(orderManagement.InformReceivingProblem());
        

//
//        welcomeGoodbyeManagement.setEmail("ni254828@gmail.com");
//        welcomeGoodbyeManagement.setUsername("Nira Ibrahim");
//        System.out.println(welcomeGoodbyeManagement.WelcomeMessage());
//        System.out.println(welcomeGoodbyeManagement.GoodbyeMessage());
//
//
//        orderManagement.setEmail("niraibrahem99@gmail.com");
//        orderManagement.setUsername("Nira Ibrahim");
//        orderManagement.setOrderId(1000);
//        System.out.println(orderManagement.SendOrder());
//        System.out.println(orderManagement.CancelOrder());
//        System.out.println(orderManagement.ConfirmOrder());
//        System.out.println(orderManagement.InformOrderError());
//        System.out.println(orderManagement.ShipOrder());
//        System.out.println(orderManagement.InformShippingProblem());
//        System.out.println(orderManagement.ReceiveOrder());
//        System.out.println(orderManagement.InformReceivingProblem());

    }
}
