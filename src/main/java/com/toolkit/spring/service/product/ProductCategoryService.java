package com.toolkit.spring.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.toolkit.spring.model.table.product.ProductCategory;
import com.toolkit.spring.repository.table.product.ProductCategoryRepository;
import com.toolkit.spring.service.OperationService;
import com.toolkit.spring.service.OptionService;
import com.toolkit.spring.service.ReadService;

@Service
public class ProductCategoryService implements
    ReadService<ProductCategory, Integer>,
    OptionService<ProductCategory, Integer>,
    OperationService<ProductCategory, Integer>
{
    @Autowired
    private ProductCategoryRepository repository;
    
    @Override
    public JpaRepository<ProductCategory, Integer> getReadRepository()
    { return repository; }

    @Override
    public JpaRepository<ProductCategory, Integer> getOperationRepository()
    { return repository; }
}
