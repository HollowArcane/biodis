package com.toolkit.spring.model.view.product;

import com.toolkit.spring.model.ValueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "v_label_product")
public class VLabelProduct implements ValueObject<Integer>
{
    @Id
    private Integer id;

    @Column(name = "product")
    private String product;

    @Column(name = "id_product_subcategory")
    private int idProductSubcategory;

    @Column(name = "product_subcategory")
    private String productSubcategory;

    @Column(name = "id_product_category")
    private int idProductCategory;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "threshold_warning")
    private double thresholdWarning;

    @Column(name = "quantity_in")
    private double quantityIn;

    @Column(name = "quantity_out")
    private double quantityOut;

    public Integer getId()
    { return id; }

    public void setId(Integer id)
    { this.id = id; }

    public String getProduct()
    { return product; }

    public void setProduct(String product)
    { this.product = product; }

    public int getIdProductSubcategory()
    { return idProductSubcategory; }

    public void setIdProductSubcategory(int idProductSubcategory)
    { this.idProductSubcategory = idProductSubcategory; }

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

    public double getThresholdWarning()
    { return thresholdWarning; }

    public void setThresholdWarning(double thresholdWarning)
    { this.thresholdWarning = thresholdWarning; }

    @Override
    public String getLabel()
    { return getProduct(); }

    public double getQuantityIn()
    { return quantityIn; }

    public void setQuantityIn(double quantityIn)
    { this.quantityIn = quantityIn; }

    public double getQuantityOut()
    { return quantityOut; }

    public void setQuantityOut(double quantityOut)
    { this.quantityOut = quantityOut; }

    public double getSignedBalance()
    {return getQuantityIn() - getQuantityOut(); }
}
