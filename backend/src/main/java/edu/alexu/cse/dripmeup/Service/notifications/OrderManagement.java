package edu.alexu.cse.dripmeup.Service.notifications;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderManagement extends NotificationService{
    private String filePath ;
    private String subject ;

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private void setSubject(String subject) {
        this.subject = subject;
    }

    private String getFilePath() {
        return this.filePath;
    }

    private String getSubject() {
        return this.subject;
    }

    private int orderId ;
    public int getOrderId() {
        return this.orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private String OrderManagementMessage() {
        String body = "" ;
        try {
            body = this.readFileFromResources(this.getFilePath()) ;
        }
        catch(IOException e) {
            e.printStackTrace();
            return "Error occurred while reading file.";
        }

        // making message body
        body = body.replace("[User\'s Name]" , this.getUsername()) ;
        body = body.replace("[Order Number]" ,  String.valueOf(this.getOrderId())) ;
        /*
        make queries to get order data
         */
        this.sendMessage(body , this.getSubject()) ;
        return "email was sent" ;
    }


    public String SendOrder() {
        this.setSubject("Thank You for Your Order!") ;
        this.setFilePath("file:src/main/resources/Notifications Body/MakingOrderByCustomer.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String CancelOrder() {
        this.setSubject ("Confirmation of Your Order Cancellation") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/DeletingOrder.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String ConfirmOrder(){
        this.setSubject("Your Order Has Been Confirmed!") ;
        this.setFilePath("file:src/main/resources/Notifications Body/ConfirmingOrder.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String InformOrderError() {
        this.setSubject("Issue with Your Order – Apologies for the Inconvenience") ;
        this.setFilePath("file:src/main/resources/Notifications Body/OrderPreparingProblem.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String ShipOrder() {
        this.setSubject ("Your Order Is On Its Way!") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/ShippingOrder.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String InformShippingProblem(){
        this.setSubject("Update on Your Order – Shipping Issue") ;
        this.setFilePath("file:src/main/resources/Notifications Body/ShippingProblem.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String ReceiveOrder() {
        this.setSubject("We\'d love your feedback on your recent purchase!") ;
        this.setFilePath("file:src/main/resources/Notifications Body/ReceivingOrder.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String InformReceivingProblem() {
        this.setSubject ("Your Order Has Been Returned to Us") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/ReceivingProblem.txt") ;
        return this.OrderManagementMessage() ;
    }
}
