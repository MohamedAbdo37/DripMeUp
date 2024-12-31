package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.dto.CartFavoriteProductDTO;
import edu.alexu.cse.dripmeup.dto.CartDTO;
import edu.alexu.cse.dripmeup.entity.CartEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.exception.CartException.CartConflictException;
import edu.alexu.cse.dripmeup.exception.CartException.CartException;
import edu.alexu.cse.dripmeup.exception.CartException.CartNotFoundException;
import edu.alexu.cse.dripmeup.exception.FavoriteException;
import edu.alexu.cse.dripmeup.repository.CartRepository;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private VariantRepository variantRepository ;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private CartRepository cartRepository ;

    private UserEntity validateUser(Long userID){

        UserEntity user = userRepository.findByUserID(userID) ;
        // check if user doesn't exist
        if(user == null)
            throw new CartNotFoundException("user doesn't exist.");
        return user ;
    }

    private VariantEntity validateVariant(Long variantID){

        VariantEntity variant = variantRepository.findByVariantID(variantID) ;
        // check if variant doesn't exist
        if(variant == null)
            throw new CartNotFoundException("variant doesn't exist.");
        return variant ;
    }
    public void validateAmount(VariantEntity variant , int amount){

        // check if amount is valid
        if(amount <= 0)
            throw new CartConflictException("amount couldn't be < 1.") ;

        // check if amount in stock is enough
        if(variant.getStock() < amount )
            throw new CartConflictException("stock isn't enough.") ;



    }

    public String addElement(Long userID , Long variantID , int amount) throws CartException {

        // validate parameters and throw exception if it isn't valid
        UserEntity user = validateUser(userID) ;
        VariantEntity variant = validateVariant(variantID) ;
        this.validateAmount(variant , amount);

        CartEntity cart = cartRepository.findByUserAndVariant(user , variant) ;

        // if it exists in cart , update it
        if(cart != null) {
            // update amount and time
            cart.setAmount(amount);
            cart.onUpdate();
            cartRepository.save(cart) ;
            return "Element has been added successfully." ;

        }
        cart = new CartEntity(user , variant , amount) ;
        cartRepository.save(cart) ;
        return "Element has been added successfully." ;
    }

    public String updateElement(Long userID , Long variantID , int amount) throws CartException {

        // validate parameters and throw exception if it isn't valid
        UserEntity user = validateUser(userID) ;
        VariantEntity variant = validateVariant(variantID) ;
        this.validateAmount(variant , amount);

        // if information is valid search if it exists in cart already
        CartEntity cart = cartRepository.findByUserAndVariant(user , variant) ;

        // if it exists in cart , update it
        if(cart != null) {
            // update amount and time
            cart.setAmount(amount);
            cart.onUpdate();
            cartRepository.save(cart) ;
            return "Element has been updated successfully." ;
        }
        // if no throw not found exception
        throw new CartNotFoundException("element doesn't exist in the cart of user.") ;
    }

    public String deleteElement(Long userID , Long variantID ) throws CartException{

        // validate parameters and throw exception if it isn't valid
        UserEntity user = validateUser(userID) ;
        VariantEntity variant = validateVariant(variantID) ;

        // try to delete that record
        Long countRecordsDeleted = cartRepository.deleteByUserAndVariant(user , variant) ;
        // if number of deleted records is zero (record doesn't exist) throw not found exception
        if(countRecordsDeleted == 0L)
            throw new CartNotFoundException("element doesn't exist in the cart of user.");
        return "Element has been deleted successfully.";
    }

    // delete all elements from cart
    public String emptyCart(Long userid) throws CartException{

        // get user record from database
        UserEntity user = this.validateUser(userid);

        // try to delete all record
        Long countRecordsDeleted = cartRepository.deleteAllByUser(user) ;

        // if number of deleted records is zero (no records exist) throw not found exception
        if(countRecordsDeleted == 0)
            throw new CartNotFoundException("No elements to be deleted from cart.") ;
        return  countRecordsDeleted +" elements have been deleted.";
    }



    public List<CartDTO> getCart(Long userID)throws CartException{

        // get user record from database
        UserEntity user = this.validateUser(userID);

        // return all cart records orders DESC
        List<CartEntity> cartElements = cartRepository.findAllByUserOrderByTimeDesc(user) ;
        // map cart records to CartDTO
        return mapToCartDTO(cartElements) ;
    }

    // map to cartDTO
    private List<CartDTO> mapToCartDTO(List<CartEntity> cartElements){
        List<CartDTO> cart = new ArrayList<>();
        // convert each item to CartDTO
        // CartDTO consists of CartFavoriteProductDTO and amount of each product
        for(CartEntity item : cartElements){
            cart.add(new CartDTO(mapToCartProductDto(item) , item.getAmount())) ;
        }
        return cart ;
    }

    // convert cart record to CartFavoriteProductDTO
    private CartFavoriteProductDTO mapToCartProductDto(CartEntity item){
        VariantEntity variant = item.getVariant() ;
        ProductEntity product = variant.getProduct();
        List<VariantImageEntity> variantImages =variant.getVariantImages();
        List<String> imagesPaths = new LinkedList<>() ;

        // get all images of variant
        for(VariantImageEntity image : variantImages)
            imagesPaths.add(image.getImagePath()) ;

        // add product , variant , images information
        CartFavoriteProductDTO productDTO = new CartFavoriteProductDTO(variant.getVariantID() , product.getProductID() , product.getDescription() ,
                variant.getColor() , variant.getLength() , variant.getWeight() ,
                variant.getSize() , variant.getPrice() , variant.getState(),
                variant.getStock() , imagesPaths ) ;

        return productDTO ;
    }

}
