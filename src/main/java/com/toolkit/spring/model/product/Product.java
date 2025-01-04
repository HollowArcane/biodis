package com.toolkit.spring.model.product;

import com.toolkit.spring.annotation.Exist;
import com.toolkit.spring.annotation.Unique;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    @NotBlank(message = "le libellé ne doit pas être vide.")
    private String label;

    @Column(name = "id_product_subcategory")
    @NotNull(message = "la sous-catégorie de produit est obligatoire.")
    @Exist(table = "product_subcategory", column = "id", message = "la sous-catégorie de produit doit être une sous-categorie valide.")
    private Integer idProductSubcategory;

    @Transient
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_product_subcategory", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductSubcategory productSubcategory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getIdProductSubcategory() {
        return idProductSubcategory;
    }

    public void setIdProductSubcategory(int idProductSubcategory) {
        this.idProductSubcategory = idProductSubcategory;
    }

    public ProductSubcategory getProductSubcategory() {
        return productSubcategory;
    }
}
