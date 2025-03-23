package com.toolkit.spring.repository.table.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toolkit.spring.model.table.product.ProductCategory;


public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>
{
    @Query(nativeQuery = true, value = "SELECT * FROM product_category WHERE id=:idProductCategory")
    List<ProductCategory> findAll(int idProductCategory);
}
