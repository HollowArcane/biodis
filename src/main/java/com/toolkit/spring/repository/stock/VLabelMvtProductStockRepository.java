package com.toolkit.spring.repository.stock;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolkit.spring.model.stock.VLabelMvtProductStock;

public interface VLabelMvtProductStockRepository extends JpaRepository<VLabelMvtProductStock, Integer> {}
