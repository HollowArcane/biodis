package com.toolkit.spring.repository.table.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolkit.spring.model.table.stock.MvtProductStock;


public interface MvtProductStockRepository extends JpaRepository<MvtProductStock, Integer> {}
