package com.toolkit.spring.repository.table.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import com.toolkit.spring.model.table.product.ProductSubcategory;


public interface ProductSubcategoryRepository extends JpaRepository<ProductSubcategory, Integer>
{
    @NativeQuery("SELECT * FROM product_subcategory WHERE id_product_category=?")
    List<ProductSubcategory> findAllByIdCategory(Integer idCategory);
}
