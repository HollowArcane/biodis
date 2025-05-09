package com.toolkit.spring.repository.table.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import com.toolkit.spring.model.table.product.Product;


public interface ProductRepository extends JpaRepository<Product, Integer>
{
    @NativeQuery("SELECT * FROM product WHERE id_product_subcategory=?")
    List<Product> findAllByIdSubcategory(Integer subcategory);
}
