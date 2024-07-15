package com.project.ecommerce.service;

import com.project.ecommerce.dao.CustomerRepository;
import com.project.ecommerce.dto.Purchase;
import com.project.ecommerce.dto.PurchaseResponse;
import com.project.ecommerce.entity.Customer;
import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{

    private final CustomerRepository customerRepository;
    @Override
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate order with billingAddress and shippingAddress
        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();

        String email = customer.getEmail();

        Customer customerFromDB = customerRepository.findByEmail(email);

        if (customerFromDB != null){
            customer = customerFromDB;
        }
        customer.add(order);

        // save to the database
        customerRepository.save(customer);


        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
    // generate a random UUID number
        return UUID.randomUUID().toString();
    }
}
