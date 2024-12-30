package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.CartEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
            UserEntity mockUser = new UserEntity() ;
            mockUser.setEmail("ni254828@gmail.com");
            mockUser.setUserID(1L);

            // return non null user
            when(userRepository.findByUserID(1L)).thenReturn(mockUser);

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
            UserEntity mockUser = new UserEntity() ;
            mockUser.setEmail("ni254828@gmail.com");
            mockUser.setUserID(1L);

            when(userRepository.findByUserID(1L)).thenReturn(mockUser);


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
            // not null user
            UserEntity mockUser = new UserEntity() ;
            mockUser.setEmail("ni254828@gmail.com");
            mockUser.setUserID(1L);

            // mock getting user
            when(userRepository.findByUserID(1L)).thenReturn(mockUser);

            // not null variant
            VariantEntity mockVariant = new VariantEntity() ;
            mockVariant.setVariantID(1L);
            mockVariant.setStock(1);

            // mock getting variant
            when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

            cartService.addElement(1L , 1L , 0);
        }
        catch (CartException e){
            assertEquals("amount couldn't be < 1.", e.getMessage());
        }
    }


    @Test
    void testAddElementToCartNotPreviouslyExisting() {
        // not null user
        UserEntity mockUser = new UserEntity() ;
        mockUser.setEmail("ni254828@gmail.com");
        mockUser.setUserID(1L);

        // mock getting user
        when(userRepository.findByUserID(1L)).thenReturn(mockUser);

        // not null variant
        VariantEntity mockVariant = new VariantEntity() ;
        mockVariant.setVariantID(1L);
        mockVariant.setStock(1);

        // mock getting variant
        when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

        CartEntity mockCart = new CartEntity(mockUser , mockVariant , 1) ;
        // mock getting cart with user and variant
        when(cartRepository.findByUserAndVariant(mockUser , mockVariant)).thenReturn(null);

        when(cartRepository.save(mockCart)).thenReturn(mockCart);

        cartService.addElement(1L , 1L , 1) ;

    }


    @Test
    void testAddElementToCartPreviouslyExisting() {
        // not null user
        UserEntity mockUser = new UserEntity() ;
        mockUser.setEmail("ni254828@gmail.com");
        mockUser.setUserID(1L);

        // mock getting user
        when(userRepository.findByUserID(1L)).thenReturn(mockUser);

        // not null variant
        VariantEntity mockVariant = new VariantEntity() ;
        mockVariant.setVariantID(1L);
        mockVariant.setStock(3);

        // mock getting variant
        when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

        CartEntity mockCart = new CartEntity(mockUser , mockVariant , 1) ;
        mockCart.setTime(LocalDateTime.now());
        LocalDateTime past = mockCart.getTime() ;
        // mock getting cart with user and variant
        when(cartRepository.findByUserAndVariant(mockUser , mockVariant)).thenReturn(mockCart);

        when(cartRepository.save(mockCart)).thenReturn(mockCart);

        cartService.addElement(1L , 1L , 2) ;
        assertEquals(mockCart.getAmount() , 2);
        assertTrue(past.isBefore(mockCart.getTime())) ;
    }

    @Test
    void updateNotExistingElement(){
        try {
            UserEntity mockUser = new UserEntity();
            mockUser.setEmail("ni254828@gmail.com");
            mockUser.setUserID(1L);
            when(userRepository.findByUserID(1L)).thenReturn(mockUser);


            VariantEntity mockVariant = new VariantEntity();
            mockVariant.setVariantID(1L);
            mockVariant.setStock(1);
            when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

            when(cartRepository.findByUserAndVariant(mockUser, mockVariant)).thenReturn(null);

            cartService.updateElement(1L, 1L, 1);
        }
        catch (CartException e){
            assertEquals("element doesn't exist in the cart of user." , e.getMessage());
        }
    }

    @Test
    void updateExistingElement(){
        // mock user
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("ni254828@gmail.com");
        mockUser.setUserID(1L);
        when(userRepository.findByUserID(1L)).thenReturn(mockUser);

        // mock variant
        VariantEntity mockVariant = new VariantEntity();
        mockVariant.setVariantID(1L);
        mockVariant.setStock(3);
        when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

        // mock cart
        CartEntity mockCart = new CartEntity(mockUser , mockVariant , 1) ;
        mockCart.setTime(LocalDateTime.now());
        LocalDateTime past = mockCart.getTime() ;
        when(cartRepository.findByUserAndVariant(mockUser, mockVariant)).thenReturn(mockCart);

        when(cartRepository.save(mockCart)).thenReturn(mockCart);

        cartService.updateElement(1L, 1L, 2);

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
            UserEntity mockUser = new UserEntity() ;
            mockUser.setEmail("ni254828@gmail.com");
            mockUser.setUserID(1L);

            // return non null user
            when(userRepository.findByUserID(1L)).thenReturn(mockUser);

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
            UserEntity mockUser = new UserEntity() ;
            mockUser.setEmail("ni254828@gmail.com");
            mockUser.setUserID(1L);
            when(userRepository.findByUserID(1L)).thenReturn(mockUser);


            VariantEntity mockVariant = new VariantEntity() ;
            mockVariant.setVariantID(1L);
            mockVariant.setStock(1);
            when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

            when(cartRepository.deleteByUserAndVariant(mockUser , mockVariant)).thenReturn(0L) ;

            cartService.deleteElement(1L , 1L) ;
        }
        catch (CartException e){
            assertEquals("element doesn't exist in the cart of user.", e.getMessage());
        }
    }

    @Test
    void testDeleteElementPreviouslyExisting() throws CartException{
        UserEntity mockUser = new UserEntity() ;
        mockUser.setEmail("ni254828@gmail.com");
        mockUser.setUserID(1L);

        when(userRepository.findByUserID(1L)).thenReturn(mockUser);


        VariantEntity mockVariant = new VariantEntity() ;
        mockVariant.setVariantID(1L);
        mockVariant.setStock(1);
        when(variantRepository.findByVariantID(1L)).thenReturn(mockVariant);

        when(cartRepository.deleteByUserAndVariant(mockUser , mockVariant)).thenReturn(1L) ;

        assertEquals(cartService.deleteElement(1L , 1L) , 1L) ;
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
            UserEntity mockUser = new UserEntity() ;
            mockUser.setEmail("ni254828@gmail.com");
            mockUser.setUserID(1L);

            when(userRepository.findByUserID(1L)).thenReturn(mockUser);

            when(cartRepository.deleteAllByUser(mockUser)).thenReturn(0L) ;

            cartService.emptyCart(1L) ;
        }
        catch (CartException e){
            assertEquals("No elements to be deleted from cart." , e.getMessage());
        }
    }

    @Test
    void testEmptyCartPreviouslyExisting() throws CartException{
        UserEntity mockUser = new UserEntity() ;
        mockUser.setEmail("ni254828@gmail.com");
        mockUser.setUserID(1L);

        when(userRepository.findByUserID(1L)).thenReturn(mockUser);

        when(cartRepository.deleteAllByUser(mockUser)).thenReturn(5L) ;

        assertEquals(cartService.emptyCart(1L) , 5L) ;
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
    void testGetCart() {
        UserEntity mockUser = new UserEntity() ;
        mockUser.setEmail("ni254828@gmail.com");
        mockUser.setUserID(1L);

        when(userRepository.findByUserID(1L)).thenReturn(mockUser);

        VariantEntity var = new VariantEntity() ;
        var.setVariantID(1L);
        var.setStock(1);
        var.setProduct(new ProductEntity());
        var.setVariantImages(new LinkedList<>()) ;

        List<CartEntity> mockCart = new ArrayList<>() ;
        mockCart.add(new CartEntity(mockUser , var , 1));

        when(cartRepository.findAllByUserOrderByTimeDesc(mockUser)).thenReturn(mockCart) ;

        cartService.getCart(1L) ;
    }

}
