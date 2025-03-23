package com.toolkit.spring.model.table.product;

import com.toolkit.spring.annotation.Exist;
import com.toolkit.spring.model.ValueObject;

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
@Table(name = "product_subcategory")
public class ProductSubcategory implements ValueObject<Integer>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    @NotBlank(message = "le libellé ne doit pas être vide.")
    private String label;

    @Column(name = "id_product_category")
    @NotNull(message = "la catégorie de produit est obligatoire.")
    @Exist(table = "product_category", column = "id", message = "la catégorie de produit doit être une categorie valide.")
    private Integer idProductCategory;

    @Transient
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_product_category", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductCategory productCategory;

    public Integer getId()
    { return id; }

    public void setId(Integer id)
    { this.id = id; }

    public String getLabel()
    { return label; }

    public void setLabel(String label)
    { this.label = label; }

    public Integer getIdProductCategory()
    { return idProductCategory; }

    public void setIdProductCategory(int idProductCategory)
    { this.idProductCategory = idProductCategory; }

    public ProductCategory getProductCategory()
    { return productCategory; }

}
