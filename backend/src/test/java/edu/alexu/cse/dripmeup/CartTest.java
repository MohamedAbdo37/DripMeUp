package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.CartEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.exception.CartException.CartException;
import edu.alexu.cse.dripmeup.repository.CartRepository;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import edu.alexu.cse.dripmeup.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartTest {
    @Mock
    @Autowired
    private VariantRepository variantRepository ;

    @Mock
    @Autowired
    private UserRepository userRepository ;

    @Mock
    @Autowired
    private CartRepository cartRepository ;

    @InjectMocks
    @Autowired
    private CartService cartService ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testAddElementToCartWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            cartService.addElement(1L , 1L , 1);
        }
        catch (CartException e){
            assertEquals("user doesn't exist." , e.getMessage());
        }
    }

    @Test
    void testAddElementToCartWithNullVariant() {
        try {
            // return non null user
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            //return null with variant
            when(variantRepository.findByVariantID(1L)).thenReturn(null);

            cartService.addElement(1L , 1L , 1);
        }
        catch (CartException e){
            assertEquals("variant doesn't exist.", e.getMessage());
        }
    }

    @Test
    void testAddElementToCartWithInValidAmount() {
        try {
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            VariantEntity mockVariant = new VariantEntity() ;
            mockVariant.setVariantID(1L);
            mockVariant.setStock(1);
            when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

            cartService.addElement(1L , 1L , 3);
        }
        catch (CartException e){
            assertEquals("stock isn't enough.", e.getMessage());
        }
    }

    @Test
    void testAddElementToCartWithInValidAmount2() {
        try {
            // mock getting user
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            // not null variant
            // mock getting variant
            when(variantRepository.findByVariantID(1L)).thenReturn(new VariantEntity());

            cartService.addElement(1L , 1L , 0);
        }
        catch (CartException e){
            assertEquals("amount couldn't be < 1.", e.getMessage());
        }
    }


    @Test
    void testAddElementToCart() {

        // not null user
        // mock getting user
        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        // not null variant
        VariantEntity mockVariant = new VariantEntity() ;
        mockVariant.setVariantID(1L);
        mockVariant.setStock(1);

        // mock getting variant
        when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

        when(cartRepository.save(new CartEntity(new UserEntity() , mockVariant , 1))).thenReturn(new CartEntity());

        assertEquals(cartService.addElement(1L , 1L , 1)
                , "Element has been added successfully.") ;

    }

    @Test
    void updateNotExistingElement(){
        try {

            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            VariantEntity mockVariant = new VariantEntity();
            mockVariant.setVariantID(1L);
            mockVariant.setStock(1);
            when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

            when(cartRepository.findByUserAndVariant(new UserEntity(), mockVariant)).thenReturn(null);

            cartService.updateElement(1L, 1L, 1);
        }
        catch (CartException e){
            assertEquals("element doesn't exist in the cart of user." , e.getMessage());
        }
    }

    @Test
    void updateExistingElement(){
        // mock user

        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        // mock variant
        VariantEntity mockVariant = new VariantEntity();
        mockVariant.setVariantID(1L);
        mockVariant.setStock(3);
        when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

        // mock cart
        CartEntity mockCart = new CartEntity(new UserEntity() , mockVariant , 1) ;
        mockCart.setTime(LocalDateTime.parse("2024-12-30T09:47:15" , DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println(mockCart.getTime());
        LocalDateTime past = mockCart.getTime() ;
        when(cartRepository.findByUserAndVariant(new UserEntity(), mockVariant)).thenReturn(mockCart);

        when(cartRepository.save(mockCart)).thenReturn(mockCart);

        assertEquals(cartService.updateElement(1L, 1L, 2)
                ,"Element has been updated successfully.");

        System.out.println(mockCart.getTime());

        assertEquals(mockCart.getAmount() , 2);
        assertTrue(past.isBefore(mockCart.getTime())) ;
    }

    @Test
    void testDeleteElementWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            cartService.deleteElement(1L , 1L);
        }
        catch (CartException e){
            assertEquals("user doesn't exist." , e.getMessage());
        }
    }

    @Test
    void testDeleteElementWithNullVariant() {
        try {

            // return non null user
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            //return null with variant
            when(variantRepository.findByVariantID(1L)).thenReturn(null);

            cartService.deleteElement(1L , 1L);
        }
        catch (CartException e){
            assertEquals("variant doesn't exist.", e.getMessage());
        }
    }

    @Test
    void testDeleteElementPreviouslyNotExisting() {
        try {

            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            when(variantRepository.findByVariantID(1L)).thenReturn(new VariantEntity());

            when(cartRepository.deleteByUserAndVariant(new UserEntity() , new VariantEntity())).thenReturn(0L) ;

            cartService.deleteElement(1L , 1L) ;
        }
        catch (CartException e){
            assertEquals("element doesn't exist in the cart of user.", e.getMessage());
        }
    }

    @Test
    void testDeleteElementPreviouslyExisting() throws CartException{

        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        when(variantRepository.findByVariantID(1L)).thenReturn(new VariantEntity());

        when(cartRepository.deleteByUserAndVariant(new UserEntity() , new VariantEntity())).thenReturn(1L) ;

        assertEquals(cartService.deleteElement(1L , 1L) , "Element has been deleted successfully.") ;
    }

    @Test
    void testEmptyCartWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            cartService.emptyCart(1L);
        }
        catch (CartException e){
            assertEquals("user doesn't exist." , e.getMessage());
        }
    }

    @Test
    void testEmptyCartPreviouslyNotExisting() {
        try {
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            when(cartRepository.deleteAllByUser(new UserEntity())).thenReturn(0L) ;

            cartService.emptyCart(1L) ;
        }
        catch (CartException e){
            assertEquals("No elements to be deleted from cart." , e.getMessage());
        }
    }

    @Test
    void testEmptyCartPreviouslyExisting() throws CartException{

        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        Long count = Math.abs(new Random().nextLong())+1;
        when(cartRepository.deleteAllByUser(new UserEntity())).thenReturn(count) ;

        assertEquals(cartService.emptyCart(1L) , count+" elements have been deleted.") ;
    }

    @Test
    void testGetCartWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            cartService.getCart(1L);
        }
        catch (CartException e){
            assertEquals("user doesn't exist." , e.getMessage());
        }
    }
    @Test
    void testGetCartWhenItIsEmpty() {

        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());
        when(cartRepository.findAllByUserOrderByTimeDesc(new UserEntity())).thenReturn(new ArrayList<CartEntity>()) ;
        cartService.getCart(1L) ;
    }

}
