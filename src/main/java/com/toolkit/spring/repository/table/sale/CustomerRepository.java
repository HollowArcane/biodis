package com.toolkit.spring.repository.table.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toolkit.spring.model.table.sale.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {}
