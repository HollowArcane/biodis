package com.toolkit.spring.model.stock;

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
    private int id;

    @Column(name = "id_product")
    private int idProduct;

    @Column
    private String product;

    @Column(name = "id_product_subcategory")
    private int idProductSubcategory;

    @Column(name = "product_subcategory")
    private String productSubcategory;

    @Column(name = "id_product_category")
    private int idProductCategory;

    @Column(name = "product_category")
    private String productCategory;

    @Column
    private double quantity;

    @Column
    private LocalDate date;

    public int getId()
    {
        return id;
    }

    public int getIdProduct()
    {
        return idProduct;
    }

    public String getProduct()
    {
        return product;
    }

    public int getIdProductSubcategory()
    {
        return idProductSubcategory;
    }

    public String getProductSubcategory()
    {
        return productSubcategory;
    }

    public int getIdProductCategory()
    {
        return idProductCategory;
    }

    public String getAction()
    {
        return quantity < 0 ? Action.WITHDRAW.getLabel(): Action.ENTRY.getLabel();
    }

    public int getIdAction()
    {
        return quantity < 0 ? Action.WITHDRAW.getId(): Action.ENTRY.getId();
    }

    public String getProductCategory()
    {
        return productCategory;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public double getAbsoluteQuantity()
    { return Math.abs(quantity); }

    public LocalDate getDate()
    {
        return date;
    }
}
