package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.enumeration.orderStatus;
import edu.alexu.cse.dripmeup.enumeration.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMetaDTO {
    private Long id;
    private orderStatus status;
    private LocalDateTime timeStamp;
    private String address;
    private double totalPrice;
    private String CustomerName;
    private String cvv;
    private String cardHolder;
    private String cardNumber;
    private String expirationDate;
    private PaymentMethod paymentMethod;
}
