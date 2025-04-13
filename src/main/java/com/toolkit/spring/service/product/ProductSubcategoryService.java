package com.toolkit.spring.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolkit.spring.model.table.product.ProductSubcategory;
import com.toolkit.spring.model.view.product.VLabelProductSubcategory;
import com.toolkit.spring.repository.table.product.ProductSubcategoryRepository;
import com.toolkit.spring.repository.view.product.VLabelProductSubcategoryRepository;
import com.toolkit.spring.service.interfaces.OperationService;
import com.toolkit.spring.service.interfaces.OptionService;
import com.toolkit.spring.service.interfaces.ReadService;

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