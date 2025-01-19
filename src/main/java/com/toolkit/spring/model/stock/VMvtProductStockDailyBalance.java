package com.toolkit.spring.model.stock;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(VMvtProductStockDailyBalanceIdClass.class)
@Table(name = "v_mvt_product_stock_daily_balance")
public class VMvtProductStockDailyBalance
{
    @Id
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

    @Column(name = "quantity_withdraw")
    private double quantityWithdraw;

    @Column(name = "quantity_entry")
    private double quantityEntry;

    @Column(name = "quantity_balance")
    private double quantityBalance;

    @Id
    @Column
    private LocalDate date;

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

    public double getQuantityWithdraw() {
        return quantityWithdraw;
    }

    public double getQuantityEntry() {
        return quantityEntry;
    }

    public double getQuantityBalance() {
        return quantityBalance;
    }

    public String getProductCategory()
    {
        return productCategory;
    }

    public LocalDate getDate()
    {
        return date;
    }
}
