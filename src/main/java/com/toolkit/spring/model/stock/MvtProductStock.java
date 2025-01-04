package com.toolkit.spring.model.stock;

import java.time.LocalDate;
import com.toolkit.spring.annotation.Exist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "mvt_product_stock")
public class MvtProductStock
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_product")
    @NotNull(message = "le produit est obligatoire.")
    @Exist(table = "product", column = "id", message = "le produit doit être un produit valide.")
    private int idProduct;
    
    @Column
    @NotNull(message = "la quantité est obligatoire")
    private double quantity;

    @Column
    @NotNull(message = "la date est obligatoire.")
    private LocalDate date = LocalDate.now();

    @Transient
    private String action;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        if(this.action == null)
        {
            this.quantity = quantity;
            this.action = quantity < 0 ? Action.WITHDRAW.getLabel(): Action.ENTRY.getLabel();
        }
        else
        {
            this.quantity = Math.abs(quantity) * (this.action.equals(Action.WITHDRAW.getLabel()) ? -1: 1);
        }
    }

    public double getAbsoluteQuantity()
    {
        return Math.abs(quantity);
    }

    public void setAction(String action)
    {
        this.action = Action.from(action).getLabel();
        quantity = Math.abs(quantity) * (action.equals("withdraw") ? 1: -1);
    }

    public String getAction()
    {
        return action;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
