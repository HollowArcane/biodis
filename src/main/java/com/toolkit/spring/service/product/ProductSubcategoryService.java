package com.toolkit.spring.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolkit.spring.model.table.product.ProductSubcategory;
import com.toolkit.spring.model.view.product.VLabelProductSubcategory;
import com.toolkit.spring.repository.table.product.ProductSubcategoryRepository;
import com.toolkit.spring.repository.view.product.VLabelProductSubcategoryRepository;
import com.toolkit.spring.service.OperationService;
import com.toolkit.spring.service.OptionService;
import com.toolkit.spring.service.ReadService;

@Service
public class ProductSubcategoryService implements
    ReadService<VLabelProductSubcategory, Integer>,
    OptionService<VLabelProductSubcategory, Integer>,
    OperationService<ProductSubcategory, Integer>
{
    @Autowired
    ProductSubcategoryRepository createRepository;
    
    @Autowired
    VLabelProductSubcategoryRepository readRepository;
    
    public VLabelProductSubcategoryRepository getReadRepository()
    { return readRepository; }

    @Override
    public ProductSubcategoryRepository getOperationRepository()
    { return createRepository; }
}