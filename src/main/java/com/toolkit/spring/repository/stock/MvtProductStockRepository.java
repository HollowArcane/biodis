package com.toolkit.spring.repository.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolkit.spring.model.stock.MvtProductStock;

public interface MvtProductStockRepository extends JpaRepository<MvtProductStock, Integer> {}
