package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.enumeration.orderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMetaDTO {
    private Long id;
    private orderStatus status;
    private Date timeStamp;
    private String address;
    private float totalPrice;
    private String CustomerName;
}
