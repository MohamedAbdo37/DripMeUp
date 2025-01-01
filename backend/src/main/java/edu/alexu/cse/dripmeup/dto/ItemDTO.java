package edu.alexu.cse.dripmeup.dto;

import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String ProductName;
    private String ProductVariantSize;
    private String ProductVariantColor;
    private double ProductVariantPrice;
    private Integer ProductVariantQuantity;
    private double ItemTotalPrice;
    private List<String> images = new LinkedList<>();

}
