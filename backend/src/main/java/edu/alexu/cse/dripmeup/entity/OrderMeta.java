package edu.alexu.cse.dripmeup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.alexu.cse.dripmeup.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "ORDER_META")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "orderMetaId")
    private Long id;

    @Column(name = "status")
    private Status status;

    @Column(name = "timeStamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    @Column(name = "address")
    private String address;

    @Column(name = "totalPrice")
    private String totalPrice;

    @ManyToOne
    @JoinColumn(name = "userID")
    private UserEntity userEntity;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;




}
