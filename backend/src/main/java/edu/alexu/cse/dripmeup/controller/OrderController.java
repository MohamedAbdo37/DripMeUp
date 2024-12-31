package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.enumeration.orderStatus;
import edu.alexu.cse.dripmeup.exception.AuthorizationException;
import edu.alexu.cse.dripmeup.exception.BadInputException;
import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;
import edu.alexu.cse.dripmeup.service.OrderService;
import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;
import edu.alexu.cse.dripmeup.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("orders")
public class OrderController {

@Autowired
private OrderService orderService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestBody orderBody){
        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try {
            LinkedList<Long> outOfStock = new LinkedList<>() ;
            String response = orderService.convertCartToOrder(USER_ID, orderBody , outOfStock);

            if(response.equals("Order was added successfully"))
                return ResponseEntity.status(200).body(response) ;

            return ResponseEntity.status(409).body(outOfStock) ;
        }
        catch (BadInputException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestParam(required=false) Integer page,
                                         @RequestParam(required=false) Integer size,
                                         @RequestParam(required=false) orderStatus status) {
        Long USER_ID = SecurityService.getIdFromSecurityContext();
        try {
            return ResponseEntity.ok(orderService.getOrders(USER_ID, page, size, status));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while fetching orders"));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-order-details/{orderId}")
    public ResponseEntity<?> getMyOrderDetails(@PathVariable Long orderId) {
        Long USER_ID = SecurityService.getIdFromSecurityContext();
        try {
            return ResponseEntity.ok(orderService.getOrderDetails(USER_ID, orderId));
        }
        catch (AuthorizationException e){
            return ResponseEntity.status(401).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (BadInputException e){
            return ResponseEntity.status(400).body(ResponseBodyMessage.error("Order does not exist"));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while fetching order details"));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getOrders(@RequestParam(required=false) Integer page,
                                         @RequestParam(required=false) Integer size,
                                         @RequestParam(required=false) orderStatus status) {
        try {
            return ResponseEntity.ok(orderService.getOrders(page, size, status));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while fetching orders"));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/order-details/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(orderService.getOrderDetails(orderId));
        }
        catch (BadInputException e){
            return ResponseEntity.status(400).body(ResponseBodyMessage.error("Order does not exist"));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while fetching order details"));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/approve/{orderId}")
    public ResponseEntity<?> approveOrder(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(orderService.approveOrder(orderId));
        }
        catch (BadInputException e){
            return ResponseEntity.status(400).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (FailedToSendMailException e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while approving order"));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/deliver/{orderId}")
    public ResponseEntity<?> deliverOrder(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(orderService.deliverOrder(orderId));
        }
        catch (BadInputException e){
            return ResponseEntity.status(400).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (FailedToSendMailException e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while changing order status to delivery"));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/confirm/{orderId}")
    public ResponseEntity<?> confirmOrder(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(orderService.confirmOrder(orderId));
        }
        catch (BadInputException e){
            return ResponseEntity.status(400).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (FailedToSendMailException e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while changing order status to confirmed"));
        }
    }


}
