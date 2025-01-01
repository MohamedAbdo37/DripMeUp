package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.enumeration.ProductState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class CartFavoriteProductDTO {
    private Long variantId ;
    private Long productId ;
    private String description ;
    private String color ;
    private String length ;
    private String weight ;
    private String size ;
    private double price ;
    private ProductState state ;
    private int stock ;
    List<String> images ;

}
