package com.toolkit.spring.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolkit.spring.model.product.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {}
