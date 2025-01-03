package edu.alexu.cse.dripmeup.service;
import edu.alexu.cse.dripmeup.dto.CartFavoriteProductDTO;
import edu.alexu.cse.dripmeup.entity.FavoriteEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.exception.FavoriteException;
import edu.alexu.cse.dripmeup.repository.FavoriteRepository;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private VariantRepository variantRepository ;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private UserEntity validateUser(Long userID){

        UserEntity user = userRepository.findByUserID(userID) ;
        // check if user doesn't exist
        if(user == null)
            throw new FavoriteException("user doesn't exist.");
        return user ;
    }

    private VariantEntity validateVariant(Long variantID){

        VariantEntity variant = variantRepository.findByVariantID(variantID) ;
        // check if variant doesn't exist
        if(variant == null)
            throw new FavoriteException("variant doesn't exist.");
        return variant ;
    }

    public String addElement(Long userID , Long variantID) throws FavoriteException {

        // validate parameters and throw exception if it isn't valid
        UserEntity user = this.validateUser(userID) ;
        VariantEntity variant = this.validateVariant(variantID) ;

        // if information is valid search if it exists in favorites already
        FavoriteEntity favorite = favoriteRepository.findByUserAndVariant(user , variant) ;

        if(favorite != null) {
            return "Element exists already.";
        }
        // if no save new record
        favorite = new FavoriteEntity(user , variant) ;
        favoriteRepository.save(favorite) ;
        return "Element has been added successfully." ;
    }

    public String deleteElement(Long userID , Long variantID ) throws FavoriteException{

        UserEntity user = this.validateUser(userID) ;
        VariantEntity variant = validateVariant(variantID) ;

        // try to delete that record
        Long countRecordsDeleted = favoriteRepository.deleteByUserAndVariant(user , variant) ;
        // if number of deleted records is zero (record doesn't exist) throw not found exception
        if(countRecordsDeleted == 0L)
            throw new FavoriteException("element doesn't exist in favorites of user.");
        return "Element has been deleted successfully.";
    }

    public String clearFavorite(Long userid) throws FavoriteException{

        UserEntity user = this.validateUser(userid) ;

        // try to delete all record
        Long countRecordsDeleted = favoriteRepository.deleteAllByUser(user) ;

        // if number of deleted records is zero (no records exist) throw not found exception
        if(countRecordsDeleted == 0L)
            throw new FavoriteException("No elements to be deleted from favorites.") ;

        // else return number of records otherwise
        return  countRecordsDeleted +" elements have been deleted.";
    }

    public List<CartFavoriteProductDTO> getFavorites(Long userID) throws FavoriteException{

        UserEntity user = this.validateUser(userID) ;
        List<FavoriteEntity> favorites = favoriteRepository.findAllByUserOrderByTimeDesc(user) ;
        return mapToFavoriteDTO(favorites) ;
    }

    private List<CartFavoriteProductDTO> mapToFavoriteDTO(List<FavoriteEntity> items){
        List<CartFavoriteProductDTO> favorites = new ArrayList<>();
        for(FavoriteEntity item : items){
            favorites.add(mapToProductDto(item)) ;
        }
        return favorites ;
    }

    private CartFavoriteProductDTO mapToProductDto(FavoriteEntity item){
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
