package com.toolkit.spring.repository.stock;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolkit.spring.model.stock.VLabelMvtProductStock;
import com.toolkit.spring.model.stock.VMvtProductStockDailyBalance;

public interface VMvtProductStockDailyBalanceRepository extends JpaRepository<VMvtProductStockDailyBalance, Integer>
{
        List<VMvtProductStockDailyBalance> findAllByDate(LocalDate date);
}
