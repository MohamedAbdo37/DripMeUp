package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.dto.CartProductDTO;
import edu.alexu.cse.dripmeup.dto.CartDTO;
import edu.alexu.cse.dripmeup.entity.CartEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.exception.CartException.CartConflictException;
import edu.alexu.cse.dripmeup.exception.CartException.CartException;
import edu.alexu.cse.dripmeup.exception.CartException.CartNotFoundException;
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

    public CartEntity validate(Long userID , Long variantID , int amount){

        VariantEntity variant = variantRepository.findByVariantID(variantID) ;
        UserEntity user = userRepository.findById(userID).orElse(null) ;

        // check if user doesn't exist
        if(user == null)
            throw new CartNotFoundException("user doesn't exist.");

        // check if variant doesn't exist
        if(variant == null)
            throw new CartNotFoundException("variant doesn't exist.");

        // check if amount in stock is enough
        if(variant.getStock() < amount )
            throw new CartConflictException("stock isn't enough.") ;

        // check if amount is valid
        if(amount <= 0)
            throw new CartConflictException("amount couldn't be < 1.") ;

        // create cart entity and return it
        return new CartEntity(user , variant , amount) ;
    }

    public CartEntity addElement(Long userID , Long variantID , int amount) throws CartException {

        // validate parameters and throw exception if it isn't valid
        CartEntity temp = validate(userID , variantID , amount) ;

        // if information is valid search if it exists in cart already
        CartEntity cart = cartRepository.findByUserAndVariant(temp.getUser() , temp.getVariant()) ;

        // if it exists in cart , update it
        if(cart != null) {
            // update amount and time
            cart.setAmount(amount);
            cart.onUpdate();
            return cartRepository.save(cart) ;

        }
        // if no save new record
        return cartRepository.save(temp) ;
    }

    public CartEntity updateElement(Long userID , Long variantID , int amount) throws CartException {

        // validate parameters and throw exception if it isn't valid
        CartEntity cart = validate(userID, variantID, amount);

        // if information is valid search if it exists in cart already
        cart = cartRepository.findByUserAndVariant(cart.getUser() , cart.getVariant()) ;

        // if it exists in cart , update it
        if(cart != null) {
            // update amount and time
            cart.setAmount(amount);
            cart.onUpdate();
            return cartRepository.save(cart) ;
        }
        // if no throw not found exception
        throw new CartNotFoundException("element doesn't exist in the cart of user.") ;
    }

    public Long deleteElement(Long userID , Long variantID ){

        // get user and variant records from database
        VariantEntity variant = variantRepository.findByVariantID(variantID) ;
        UserEntity user = userRepository.findById(userID).orElse(null) ;

        // if user doesn't exist throw exception
        if(user == null)
            throw new CartNotFoundException("user doesn't exist.");

        // if variant doesn't exist throw exception
        if(variant == null)
            throw new CartNotFoundException("variant doesn't exist.");

        // try to delete that record
        Long countRecordsDeleted = cartRepository.deleteByUserAndVariant(user , variant) ;
        // if number of deleted records is zero (record doesn't exist) throw not found exception
        if(countRecordsDeleted == 0)
            throw new CartNotFoundException("element doesn't exist in the cart of user.");
        // else return number of records otherwise
        return countRecordsDeleted;
    }

    // delete all elements from cart
    public Long emptyCart(Long userid){

        // get user records from database
        UserEntity user = userRepository.findById(userid).orElse(null) ;
        // if user doesn't exist throw exception
        if(user == null)
            throw new CartNotFoundException("user doesn't exist.");

        // try to delete all record
        Long countRecordsDeleted = cartRepository.deleteAllByUser(user) ;

        // if number of deleted records is zero (no records exist) throw not found exception
        if(countRecordsDeleted == 0)
            throw new CartNotFoundException("No elements to be deleted from cart.") ;

        // else return number of records otherwise
        return countRecordsDeleted ;
    }

    public List<CartDTO> getCart(Long userID){

        // get user records from database
        UserEntity user = userRepository.findById(userID).orElse(null) ;
        // if user doesn't exist throw exception
        if(user == null)
            throw new CartNotFoundException("user doesn't exist.");

        // return all cart records orders DESC
        List<CartEntity> cartElements = cartRepository.findAllByUserOrderByTimeDesc(user) ;
        // map cart records to CartDTO
        return mapToCartDTO(cartElements) ;
    }

    // map to cartDTO
    private List<CartDTO> mapToCartDTO(List<CartEntity> cartElements){
        List<CartDTO> cart = new ArrayList<>();
        // convert each item to CartDTO
        // CartDTO consists of CartProductDTO and amount of each product
        for(CartEntity item : cartElements){
            cart.add(new CartDTO(mapToCartProductDto(item) , item.getAmount())) ;
        }
        return cart ;
    }

    // convert cart record to CartProductDTO
    private CartProductDTO mapToCartProductDto(CartEntity item){
        VariantEntity variant = item.getVariant() ;
        ProductEntity product = variant.getProduct();
        List<VariantImageEntity> variantImages =variant.getVariantImages();
        List<String> imagesPaths = new LinkedList<>() ;

        // get all images of variant
        for(VariantImageEntity image : variantImages)
            imagesPaths.add(image.getImagePath()) ;

        // add product , variant , images information
        CartProductDTO productDTO = new CartProductDTO(variant.getVariantID() , product.getProductID() , product.getDescription() ,
                variant.getColor() , variant.getLength() , variant.getWeight() ,
                variant.getSize() , variant.getPrice() , variant.getState(),
                variant.getStock() , imagesPaths ) ;

        return productDTO ;
    }

}
