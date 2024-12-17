package edu.alexu.cse.dripmeup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String ProductName;
    private String ProductVariantSize;
    private String ProductVariantColor;
    private float ProductVariantPrice;
    private Integer ProductVariantQuantity;
    private float ItemTotalPrice;
}
