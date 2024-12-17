package edu.alexu.cse.dripmeup.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersListDTO {
    private List<OrderMetaDTO> data;
    private PageMetaDTO meta;
}
