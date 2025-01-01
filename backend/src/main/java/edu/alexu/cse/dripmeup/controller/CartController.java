package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.dto.CartDTO;
import edu.alexu.cse.dripmeup.exception.CartException.CartConflictException;
import edu.alexu.cse.dripmeup.exception.CartException.CartNotFoundException;
import edu.alexu.cse.dripmeup.service.CartService;
import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;
import edu.alexu.cse.dripmeup.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")

public class CartController {

    @Autowired
    private CartService cartservice;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/cart/get")

    public ResponseEntity<?> cart(){

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            List<CartDTO> cartElements = cartservice.getCart(USER_ID) ;

            if(cartElements.size() == 0)
                return ResponseEntity.noContent().build() ;

            return ResponseEntity.status(200).body(cartElements) ;
        }
        catch (CartNotFoundException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/cart/add/{productVariantId}")
    public ResponseEntity<?> addItemToCart(@PathVariable ("productVariantId") Long variantId ,
                                           @RequestParam(required = false , defaultValue = "1") int amount) {

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            return ResponseEntity.status(200).body(ResponseBodyMessage
                            .message(cartservice.addElement(USER_ID , variantId , amount)));
        }
        catch (CartNotFoundException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (CartConflictException e){
            return ResponseEntity.status(409).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user cart.")) ;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/cart/update/{productVariantId}")
    public ResponseEntity<?> updateItemFromCart(@PathVariable ("productVariantId") Long variantId ,
                                                @RequestParam(required = false , defaultValue = "1") int amount) {

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            return ResponseEntity.status(200).body(ResponseBodyMessage
                    .message(cartservice.updateElement(USER_ID , variantId , amount)));
        }
        catch (CartNotFoundException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (CartConflictException e){
            return ResponseEntity.status(409).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user cart.")) ;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart/delete/{productVariantId}")

    public ResponseEntity<?> deleteItemFromCart(@PathVariable ("productVariantId") Long variantId ){

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            return ResponseEntity.status(200).body(ResponseBodyMessage
                    .message(cartservice.deleteElement(USER_ID , variantId)));
        }
        catch (CartNotFoundException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user cart.")) ;
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart/empty")

    public ResponseEntity<?> emptyCart(){

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            return ResponseEntity.status(200).body(ResponseBodyMessage
                    .message(cartservice.emptyCart(USER_ID)));
        }
        catch (CartNotFoundException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user cart.")) ;
        }
    }
}