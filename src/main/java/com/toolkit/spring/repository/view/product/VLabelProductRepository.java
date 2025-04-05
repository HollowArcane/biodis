package com.toolkit.spring.repository.view.product;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toolkit.spring.model.view.product.VLabelProduct;

@Repository
public interface VLabelProductRepository extends JpaRepository<VLabelProduct, Integer>
{   
    @Query(nativeQuery = true, value = "SELECT * FROM v_label_product WHERE (:idProductCategory IS NULL OR id_product_category = :idProductCategory) AND (:idProductSubcategory IS NULL OR id_product_subcategory = :idProductSubcategory)") 
    public List<VLabelProduct> findAllByCriteria(Integer idProductCategory, Integer idProductSubcategory);
}
