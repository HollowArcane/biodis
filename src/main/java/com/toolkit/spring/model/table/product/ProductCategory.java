package com.toolkit.spring.model.table.product;


import com.toolkit.spring.model.ValueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "product_category")
public class ProductCategory implements ValueObject<Integer>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotBlank(message = "le libellé ne doit pas être vide.")
    private String label;

    public Integer getId()
    { return id; }

    public String getLabel()
    { return label; }

    public void setId(Integer id)
    { this.id = id; }

    public void setLabel(String label)
    { this.label = label; }  
}
