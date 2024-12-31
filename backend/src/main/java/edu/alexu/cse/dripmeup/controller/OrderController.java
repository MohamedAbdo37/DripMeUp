package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.enumeration.Status;
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
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("orders")
public class OrderController {

@Autowired
private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestParam(required=false) Integer page,
                                         @RequestParam(required=false) Integer size,
                                         @RequestParam(required=false) Status status) {
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
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while fetching order details"));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getOrders(@RequestParam(required=false) Integer page,
                                         @RequestParam(required=false) Integer size,
                                         @RequestParam(required=false) Status status) {
        try {
            return ResponseEntity.ok(orderService.getOrders(page, size, status));
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
    @PutMapping("/approve/{orderId}")
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
    @PutMapping("/deliver/{orderId}")
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
    @PutMapping("/confirm/{orderId}")
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
