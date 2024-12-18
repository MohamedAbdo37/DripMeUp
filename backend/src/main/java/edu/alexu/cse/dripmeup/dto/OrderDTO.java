package edu.alexu.cse.dripmeup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private List<ItemDTO> items;
    private OrderMetaDTO meta;

//public OrderDTO(List<ItemDTO> items , OrderMetaDTO meta){
//        this.items = items ;
//        this.meta = meta ;
//    }
    
}
