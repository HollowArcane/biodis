package com.toolkit.spring.repository.view.product;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toolkit.spring.model.view.product.VLabelProduct;

@Repository
public interface VLabelProductRepository extends JpaRepository<VLabelProduct, Integer>
{   
    @Query(nativeQuery = true, value = "SELECT * FROM v_label_product WHERE id_product_category = :idProductCategory")
    public List<VLabelProduct> findAllByIdProductCategory(Integer idProductCategory);
}
