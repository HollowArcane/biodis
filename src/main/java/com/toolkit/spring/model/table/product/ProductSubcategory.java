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
@Table(name = "product_subcategory")
public class ProductSubcategory extends ItemEntity<Integer>
{
    @Column(name = "id_product_category")
    @NotNull(message = "la catégorie de produit est obligatoire.")
    @Exist(table = "product_category", column = "id", message = "la catégorie de produit doit être une categorie valide.")
    private Integer idProductCategory;

    public Integer getIdProductCategory()
    { return idProductCategory; }

    public void setIdProductCategory(int idProductCategory)
    { this.idProductCategory = idProductCategory; }
}
