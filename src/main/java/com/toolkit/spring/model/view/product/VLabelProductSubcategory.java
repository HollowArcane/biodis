package com.toolkit.spring.model.view.product;

import com.toolkit.spring.model.interfaces.IItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "v_label_product_subcategory")
public class VLabelProductSubcategory implements IItem<Integer>
{
    @Id
    private Integer id;

    @Column(name = "product_subcategory")
    private String productSubcategory;

    @Column(name = "id_product_category")
    private int idProductCategory;

    @Column(name = "product_category")
    private String productCategory;

    public Integer getId()
    { return id; }

    public void setId(Integer id)
    { this.id = id; }

    public String getProductSubcategory()
    { return productSubcategory; }

    public void setProductSubcategory(String productSubcategory)
    { this.productSubcategory = productSubcategory; }

    public int getIdProductCategory()
    { return idProductCategory; }

    public void setIdProductCategory(int idProductCategory)
    { this.idProductCategory = idProductCategory; }

    public String getProductCategory()
    { return productCategory; }

    public void setProductCategory(String productCategory)
    { this.productCategory = productCategory; }

    @Override
    public String getLabel()
    { return getProductSubcategory(); }    
}
