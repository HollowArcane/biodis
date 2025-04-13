package com.toolkit.spring.model.table.shared;

import com.toolkit.spring.model.interfaces.IItem;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;

@MappedSuperclass
public abstract class ItemEntity<T> implements IItem<T>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;
    
    @Column
    @NotBlank(message = "le libellé ne doit pas être vide.")
    private String label;

    public T getId()
    { return id; }

    public String getLabel()
    { return label; }

    public void setId(T id)
    { this.id = id; }

    public void setLabel(String label)
    { this.label = label; }
}
