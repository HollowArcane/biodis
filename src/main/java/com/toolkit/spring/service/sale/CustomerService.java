package com.toolkit.spring.service.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.toolkit.spring.model.table.sale.Customer;
import com.toolkit.spring.repository.table.sale.CustomerRepository;
import com.toolkit.spring.service.interfaces.OperationService;
import com.toolkit.spring.service.interfaces.ReadService;

@Service
public class CustomerService implements
    ReadService<Customer, Integer>,
    OperationService<Customer, Integer>
{
    @Autowired
    private CustomerRepository repository;

    @Override
    public JpaRepository<Customer, Integer> getOperationRepository()
    { return repository; }

    @Override
    public JpaRepository<Customer, Integer> getReadRepository()
    { return repository; }
}
