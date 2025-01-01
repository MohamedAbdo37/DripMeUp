package edu.alexu.cse.dripmeup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor

public class CartDTO {
    private CartFavoriteProductDTO element;
    private int amount ;
}
