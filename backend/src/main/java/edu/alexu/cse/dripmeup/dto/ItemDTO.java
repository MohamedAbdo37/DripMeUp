package edu.alexu.cse.dripmeup.dto;

import lombok.*;

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
