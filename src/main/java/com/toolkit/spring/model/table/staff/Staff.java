package com.toolkit.spring.model.table.staff;

import com.toolkit.spring.model.table.shared.HumanEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff extends HumanEntity<Integer>
{
}
