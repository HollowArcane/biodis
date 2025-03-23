package com.toolkit.spring.model.view.stock;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "v_label_mvt_product_stock")
public class VLabelMvtProductStock
{
    @Id
    private Integer id;

    @Column(name = "id_product")
    private Integer idProduct;

    private String product;

    @Column(name = "id_product_subcategory")
    private Integer idProductSubcategory;

    @Column(name = "product_subcategory")
    private String productSubcategory;

    @Column(name = "id_product_category")
    private Integer idProductCategory;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "threshold_warning")
    private double thresholdWarning;

    @Column(name = "quantity_in")
    private double quantityIn;

    @Column(name = "quantity_out")
    private double quantityOut;

    private LocalDate date;

    @Transient
    private String action; // unused, just to trigger json serialization of getAction()

    @Transient
    private double absoluteBalance; // unused, just to trigger json serialization of getAbsoluteBalance()


    public Integer getId()
    { return id; }

    public void setId(Integer id)
    { this.id = id; }

    public Integer getIdProduct()
    { return idProduct; }

    public void setIdProduct(Integer idProduct)
    { this.idProduct = idProduct; }

    public String getProduct()
    { return product; }

    public void setProduct(String product)
    { this.product = product; }

    public Integer getIdProductSubcategory()
    { return idProductSubcategory; }

    public void setIdProductSubcategory(Integer idProductSubcategory)
    { this.idProductSubcategory = idProductSubcategory; }

    public String getProductSubcategory()
    { return productSubcategory; }

    public void setProductSubcategory(String productSubcategory)
    { this.productSubcategory = productSubcategory; }

    public Integer getIdProductCategory()
    { return idProductCategory; }

    public void setIdProductCategory(Integer idProductCategory)
    { this.idProductCategory = idProductCategory; }

    public String getProductCategory()
    { return productCategory; }

    public void setProductCategory(String productCategory)
    { this.productCategory = productCategory; }

    public double getThresholdWarning()
    { return thresholdWarning; }

    public void setThresholdWarning(double thresholdWarning)
    { this.thresholdWarning = thresholdWarning; }

    public double getQuantityIn()
    { return quantityIn; }

    public void setQuantityIn(double quantityIn)
    { this.quantityIn = quantityIn; }

    public double getQuantityOut()
    { return quantityOut; }

    public void setQuantityOut(double quantityOut)
    { this.quantityOut = quantityOut; }

    public double getAbsoluteBalance()
    { return Math.abs(getQuantityIn() - getQuantityOut()); }

    public LocalDate getDate()
    { return date; }

    public void setDate(LocalDate date)
    { this.date = date; }
    
    public String getAction()
    {
        boolean in = getQuantityIn() > 0;
        boolean out = getQuantityOut() > 0;

        return (in ? "Entrée": "") + (in && out ? "/": "") + (out ? "Sortie": "");
    }
}
