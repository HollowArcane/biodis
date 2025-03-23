package com.toolkit.spring.repository.view.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toolkit.spring.model.view.product.VLabelProductSubcategory;

@Repository
public interface VLabelProductSubcategoryRepository extends JpaRepository<VLabelProductSubcategory, Integer> {}
