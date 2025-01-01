package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.FavoriteEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.exception.FavoriteException;
import edu.alexu.cse.dripmeup.repository.FavoriteRepository;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import edu.alexu.cse.dripmeup.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FavoriteTest {
    @Mock
    @Autowired
    private VariantRepository variantRepository ;

    @Mock
    @Autowired
    private UserRepository userRepository ;

    @Mock
    @Autowired
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    @Autowired
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testAddElementWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            favoriteService.addElement(1L , 1L);
        }
        catch (FavoriteException e){
            assertEquals("user doesn't exist." , e.getMessage());
        }
    }

    @Test
    void testAddElementWithNullVariant() {
        try {
            // return non null user
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            //return null with variant
            when(variantRepository.findByVariantID(1L)).thenReturn(null);

            favoriteService.addElement(1L , 1L);
        }
        catch (FavoriteException e){
            assertEquals("variant doesn't exist.", e.getMessage());
        }
    }

    @Test
    void testAddElementNotPreviouslyExisting() {

        // mock getting user
        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        // mock getting variant
        when(variantRepository.findByVariantID(1L)).thenReturn(new VariantEntity());

        when(favoriteRepository.findByUserAndVariant(new UserEntity() , new VariantEntity())).thenReturn(null);

        when(favoriteRepository.save(new FavoriteEntity(new UserEntity() , new VariantEntity())))
                .thenReturn(new FavoriteEntity());

        assertEquals("Element has been added successfully." , favoriteService.addElement(1L , 1L));
    }


    @Test
    void testAddElementPreviouslyExisting() {
        // not null user
        // mock getting user
        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        // mock getting variant
        when(variantRepository.findByVariantID(1L)).thenReturn(new VariantEntity());

        // mock getting variant
        when(favoriteRepository.findByUserAndVariant(new UserEntity() , new VariantEntity())).thenReturn(new FavoriteEntity());

        assertEquals("Element exists already." , favoriteService.addElement(1L , 1L));
    }

    @Test
    void testDeleteElementWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            favoriteService.deleteElement(1L , 1L);
        }
        catch (FavoriteException e){
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

            favoriteService.deleteElement(1L , 1L);
        }
        catch (FavoriteException e){
            assertEquals("variant doesn't exist.", e.getMessage());
        }
    }

    @Test
    void testDeleteElementPreviouslyNotExisting() {
        try {
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            when(variantRepository.findByVariantID(1L)).thenReturn(new VariantEntity());

            when(favoriteRepository.deleteByUserAndVariant(new UserEntity() , new VariantEntity())).thenReturn(0L) ;
            favoriteService.deleteElement(1L , 1L) ;
        }
        catch (FavoriteException e){
            assertEquals("element doesn't exist in favorites of user.", e.getMessage());
        }
    }

    @Test
    void testDeleteElementPreviouslyExisting() throws FavoriteException{

        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        when(variantRepository.findByVariantID(1L)).thenReturn(new VariantEntity());

        when(favoriteRepository.deleteByUserAndVariant(new UserEntity() , new VariantEntity())).thenReturn(1L) ;

        assertEquals(favoriteService.deleteElement(1L , 1L) , "Element has been deleted successfully.") ;
    }

    @Test
    void testEmptyFavoritesWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            favoriteService.clearFavorite(1L);
        }
        catch (FavoriteException e){
            assertEquals("user doesn't exist." , e.getMessage());
        }
    }

    @Test
    void testEmptyFavoritesPreviouslyNotExisting() {
        try {
            when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

            when(favoriteRepository.deleteAllByUser(new UserEntity())).thenReturn(0L) ;

            favoriteService.clearFavorite(1L) ;
        }
        catch (FavoriteException e){
            assertEquals("No elements to be deleted from favorites." , e.getMessage());
        }
    }

    @Test
    void testEmptyFavoritesPreviouslyExisting() throws FavoriteException{
        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());

        Long count = Math.abs(new Random().nextLong())+1;
        when(favoriteRepository.deleteAllByUser(new UserEntity())).thenReturn(count) ;

        assertEquals(favoriteService.clearFavorite(1L) , count+" elements have been deleted.") ;
    }

    @Test
    void testGetFavoritesWithNullUser() {
        try {
            // return null with user
            when(userRepository.findByUserID(1L)).thenReturn(null);
            favoriteService.getFavorites(1L);
        }
        catch (FavoriteException e){
            assertEquals("user doesn't exist." , e.getMessage());
        }
    }
    @Test
    void testGetFavoritesWhenItIsEmpty() {

        when(userRepository.findByUserID(1L)).thenReturn(new UserEntity());
        when(favoriteRepository.findAllByUserOrderByTimeDesc(new UserEntity())).thenReturn(new ArrayList<FavoriteEntity>()) ;
        favoriteService.getFavorites(1L) ;
    }

}
