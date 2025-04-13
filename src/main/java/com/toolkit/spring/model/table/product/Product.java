package com.toolkit.spring.model.table.product;

import com.toolkit.spring.annotation.Exist;
import com.toolkit.spring.model.table.shared.ItemEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product extends ItemEntity<Integer>
{
    @Column(name = "id_product_subcategory")
    @NotNull(message = "la sous-catégorie de produit est obligatoire.")
    @Exist(table = "product_subcategory", column = "id", message = "la sous-catégorie de produit doit être une sous-categorie valide.")
    private Integer idProductSubcategory;

    @Column(name = "threshold_warning")
    @NotNull(message = "le seuil de quantité minimum est obligatoire")
    private double thresholdWarning;

    public Integer getIdProductSubcategory()
    { return idProductSubcategory; }

    public double getThresholdWarning()
    { return thresholdWarning; }

    public void setIdProductSubcategory(Integer idProductSubcategory)
    { this.idProductSubcategory = idProductSubcategory; }

    public void setThresholdWarning(double thresholdWarning)
    { this.thresholdWarning = thresholdWarning; }
}
