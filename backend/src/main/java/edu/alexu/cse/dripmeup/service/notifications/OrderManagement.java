package edu.alexu.cse.dripmeup.service.notifications;
import edu.alexu.cse.dripmeup.dto.ItemDTO;
import edu.alexu.cse.dripmeup.dto.OrderDTO;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
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
                       .append(" from ").append(item.getProductName())
                       .append(" with size (").append(item.getProductVariantSize()).append(")")
                       .append(", and color ").append(item.getProductVariantColor())
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
        this.setBody(this.getBody().replace("[Item Names]" ,  itemsListToString(orderDTO.getItems())));
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
        this.setResource(new ClassPathResource("NotificationsBody/MakingOrderByCustomer.txt"));
        this.setSubject("Thank You for Your Order!") ;
        return this.OrderManagementMessage() ;
    }

    public String CancelOrder() {
        this.setResource(new ClassPathResource("NotificationsBody/DeletingOrder.txt"));
        this.setSubject ("Confirmation of Your Order Cancellation") ;
        return this.OrderManagementMessage() ;
    }

    public String ConfirmOrder(){
        this.setResource(new ClassPathResource("NotificationsBody/ConfirmingOrder.txt"));
        this.setSubject("Your Order Has Been Confirmed!") ;
        return this.OrderManagementMessage() ;
    }

    public String InformOrderError() {
        this.setResource(new ClassPathResource("NotificationsBody/OrderPreparingProblem.txt"));
        this.setSubject("Issue with Your Order – Apologies for the Inconvenience") ;
        return this.OrderManagementMessage() ;
    }

    public String ShipOrder() {
        this.setResource(new ClassPathResource("NotificationsBody/ShippingOrder.txt"));
        this.setSubject ("Your Order Is On Its Way!") ;
        return this.OrderManagementMessage() ;
    }

    public String InformShippingProblem(){
        this.setResource(new ClassPathResource("NotificationsBody/ShippingProblem.txt"));
        this.setSubject("Update on Your Order – Shipping Issue") ;
        return this.OrderManagementMessage() ;
    }

    public String ReceiveOrder() {
        this.setResource(new ClassPathResource("NotificationsBody/ReceivingOrder.txt"));
        this.setSubject("We'd love your feedback on your recent purchase!") ;
        return this.OrderManagementMessage() ;
    }

    public String InformReceivingProblem() {
        this.setResource(new ClassPathResource("NotificationsBody/ReceivingProblem.txt"));
        this.setSubject ("Your Order Has Been Returned to Us") ;
        return this.OrderManagementMessage() ;
    }
}
