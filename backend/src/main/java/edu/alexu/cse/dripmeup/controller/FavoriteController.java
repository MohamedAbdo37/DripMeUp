package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.dto.CartFavoriteProductDTO;
import edu.alexu.cse.dripmeup.exception.FavoriteException;
import edu.alexu.cse.dripmeup.service.FavoriteService;
import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;
import edu.alexu.cse.dripmeup.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")

public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/favorites/get")

    public ResponseEntity<?> favorites(){

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            List<CartFavoriteProductDTO> favorites = favoriteService.getFavorites(USER_ID) ;

            if(favorites.size() == 0)
                return ResponseEntity.noContent().build() ;

            return ResponseEntity.status(200).body(favorites) ;
        }
        catch (FavoriteException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user favorites.")) ;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/favorites/add/{productVariantId}")
    public ResponseEntity<?> addItemToFavorites(@PathVariable("productVariantId") Long variantId) {

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            return ResponseEntity.status(200).body(ResponseBodyMessage
                    .message(favoriteService.addElement(USER_ID , variantId))) ;
        }
        catch (FavoriteException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user favorites.")) ;
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/favorites/delete/{productVariantId}")

    public ResponseEntity<?> deleteItemFromFavorites(@PathVariable ("productVariantId") Long variantId ){

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            return ResponseEntity.status(200).body(ResponseBodyMessage.
                    message(favoriteService.deleteElement(USER_ID , variantId)));
        }
        catch (FavoriteException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user favorites.")) ;
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/favorites/clear")

    public ResponseEntity<?> clearFavorites(){

        Long USER_ID = SecurityService.getIdFromSecurityContext();

        try{
            return ResponseEntity.status(200).body(ResponseBodyMessage
                    .message(favoriteService.clearFavorite(USER_ID)));
        }
        catch (FavoriteException e){
            return ResponseEntity.status(404).body(ResponseBodyMessage.error(e.getMessage())) ;
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("Unable to retrieve user favorites")) ;
        }
    }


}
