package com.project.ecommerce.dao;

import com.project.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Customer findByEmail(String email);
}
