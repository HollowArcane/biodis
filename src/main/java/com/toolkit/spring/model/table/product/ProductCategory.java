package com.toolkit.spring.model.table.product;


import com.toolkit.spring.model.table.shared.ItemEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_category")
public class ProductCategory extends ItemEntity<Integer>
{
    
}
