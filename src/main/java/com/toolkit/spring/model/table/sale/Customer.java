package com.toolkit.spring.model.table.sale;

import com.toolkit.spring.model.table.shared.HumanEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer extends HumanEntity<Integer>
{
    
}
