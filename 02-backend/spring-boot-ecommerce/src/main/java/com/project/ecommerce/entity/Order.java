package com.project.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "order_tracking_number")
    private String orderTrackingNumber;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "total_quantity")
    private int totalQuantity;
    @Column(name = "status")
    private String status;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDate dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private LocalDate lastUpdated;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

    public void add(OrderItem item){
        if (item != null){
            if (orderItems == null){
                orderItems = new HashSet<>();
            }
            orderItems.add(item);
            item.setOrder(this);
        }
    }
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id",referencedColumnName = "id")
    private Address shippingAddress;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id",referencedColumnName = "id")
    private Address billingAddress;
}