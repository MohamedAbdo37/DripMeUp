package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.enumeration.PaymentMethod;
import edu.alexu.cse.dripmeup.enumeration.orderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "orderId")
    private Long id;

    @Column(name = "status")
    private orderStatus status;

    @Column(name = "timeStamp")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeStamp;

    @Column(name = "totalPrice")
    private double totalPrice;

    // many orders refer to one user
    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity userEntity;

    // one order refers to many items
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private List<OrderItem> orderItemList = new ArrayList<>();


    @Column(name = "address")
    private String address;

    @Column(name = "paymentMethod")
    private PaymentMethod paymentMethod ;

    @Column(name = "cardNumber")
    private String cardNumber ;

    @Column(name = "cardHolder")
    private String cardHolder ;

    @Column(name = "expirationDate")
    private String expirationDate ;

    @Column(name = "CVV")
    private String CVV ;

    // before saved for the first time
    @PrePersist
    protected void onCreate (){
        this.timeStamp = LocalDateTime.now() ;
    }

    public void onUpdate (){
        this.timeStamp = LocalDateTime.now() ;
    }


}
