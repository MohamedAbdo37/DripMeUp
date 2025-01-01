package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.enumeration.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequestBody {
    String address ;
    PaymentMethod paymentMethod ;
    String cardNumber ;
    String cardHolder ;
    String  expirationDate ;
    String cvv;

    @Override
    public String toString() {
        return "OrderRequestBody{" +
                "address='" + address + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", CVV='" + cvv + '\'' +
                '}';
    }
}
