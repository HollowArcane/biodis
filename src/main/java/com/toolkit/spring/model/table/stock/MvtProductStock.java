package com.toolkit.spring.model.table.stock;

import java.time.LocalDate;
import com.toolkit.spring.annotation.Exist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

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
    @NotNull(message = "la quantité d'entrée est obligatoire")
    @PositiveOrZero(message = "la quantité d'entrée ne peut pas être négative")
    private Double quantityIn;
    
    @Column
    @NotNull(message = "la quantité de sortie est obligatoire")
    @PositiveOrZero(message = "la quantité de sortie ne peut pas être négative")
    private Double quantityOut;

    @Column
    @NotNull(message = "la date est obligatoire.")
    private LocalDate date = LocalDate.now();

    public Integer getId()
    { return id; }

    public void setId(Integer id)
    { this.id = id; }

    public int getIdProduct()
    { return idProduct; }

    public void setIdProduct(int idProduct)
    { this.idProduct = idProduct; }

    public double getQuantityIn()
    { return quantityIn; }

    public void setQuantityIn(double quantityIn)
    { this.quantityIn = quantityIn; }

    public double getQuantityOut()
    { return quantityOut; }

    public void setQuantityOut(double quantityOut)
    { this.quantityOut = quantityOut; }

    public LocalDate getDate()
    { return date; }

    public void setDate(LocalDate date)
    { this.date = date; }
}
