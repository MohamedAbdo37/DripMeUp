package edu.alexu.cse.dripmeup.service.notifications;
import edu.alexu.cse.dripmeup.dto.ItemDTO;
import edu.alexu.cse.dripmeup.dto.OrderDTO;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
@Setter
@Getter
public class OrderManagement extends NotificationService{

    // specific attribute for this class
    private Long orderId ;
    private OrderDTO orderDTO ;

    private static String itemsListToString(List<ItemDTO> items) {
        StringBuilder itemsString = new StringBuilder();
        itemsString.append("\n");
        for (ItemDTO item : items) {
            itemsString.append(item.getProductVariantQuantity())
                       .append("x ").append(item.getProductName())
                       .append(" (").append(item.getProductVariantSize()).append(")")
                       .append(", ").append(item.getProductVariantColor())
                       .append("\n");
        }
        return itemsString.toString();
    }


    // IF there is an error with reading file return error else try to send message if there is an error return error
    // else return that email has been sent
    private String OrderManagementMessage() {

        try {
            this.setBody(this.readFileFromResources()) ;
        }
        catch(IOException e) {
            return "Error occurred while reading file.";
        }

        // making message body
        this.setBody(this.getBody().replace("[User's Name]" , this.getUsername()));
        this.setBody(this.getBody().replace("[Order Number]" ,  String.valueOf(this.getOrderId())));
        this.setBody(this.getBody().replace("[OrderItem Names]" ,  itemsListToString(orderDTO.getItems())));
        this.setBody(this.getBody().replace("[Order Total]" ,  String.valueOf(orderDTO.getMeta().getTotalPrice()) + " EGP"));
        /*
        make queries to get order data
         */

        if (this.sendMessage())
            return "email was sent" ;
        throw new FailedToSendMailException("Failed to send email") ;
    }


    // set body and subject of each message type

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
        this.setSubject("We'd love your feedback on your recent purchase!") ;
        this.setFilePath("file:src/main/resources/Notifications Body/ReceivingOrder.txt") ;
        return this.OrderManagementMessage() ;
    }

    public String InformReceivingProblem() {
        this.setSubject ("Your Order Has Been Returned to Us") ;
        this.setFilePath ("file:src/main/resources/Notifications Body/ReceivingProblem.txt") ;
        return this.OrderManagementMessage() ;
    }
}
