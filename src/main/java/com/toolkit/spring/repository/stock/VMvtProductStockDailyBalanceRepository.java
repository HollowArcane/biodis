package com.toolkit.spring.repository.stock;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toolkit.spring.model.stock.VLabelMvtProductStock;
import com.toolkit.spring.model.stock.VMvtProductStockDailyBalance;

public interface VMvtProductStockDailyBalanceRepository extends JpaRepository<VMvtProductStockDailyBalance, Integer>
{
        List<VMvtProductStockDailyBalance> findAllByDate(LocalDate date);

        @Query(nativeQuery = true, value = "SELECT" + //
                "    p.id AS id_product," + //
                "    p.label AS product," + //
                "    p.id_product_subcategory," + //
                "    ps.label AS product_subcategory," + //
                "    ps.id_product_category," + //
                "    pc.label AS product_category," + //
                "    COALESCE((" + //
                "        SELECT SUM(CASE WHEN _mps.quantity < 0 THEN -_mps.quantity ELSE 0 END)" + //
                "        FROM mvt_product_stock _mps" + //
                "        WHERE _mps.id_product = p.id AND _mps.date = :date" + //
                "    ), 0) AS quantity_withdraw," + //
                "    COALESCE((" + //
                "        SELECT SUM(CASE WHEN _mps.quantity > 0 THEN _mps.quantity ELSE 0 END)" + //
                "        FROM mvt_product_stock _mps" + //
                "        WHERE _mps.id_product = p.id AND _mps.date = :date" + //
                "    ), 0) AS quantity_entry," + //
                "    COALESCE((" + //
                "        SELECT SUM(_mps.quantity) " + //
                "        FROM mvt_product_stock _mps " + //
                "        WHERE _mps.id_product = p.id AND _mps.date <= :date" + //
                "    ), 0) AS quantity_balance," + //
                "    :date AS date" + //
                " FROM" + //
                "    product p" + //
                " JOIN" + //
                "    product_subcategory ps ON p.id_product_subcategory = ps.id" + //
                " JOIN" + //
                "    product_category pc ON ps.id_product_category = pc.id")
        List<VMvtProductStockDailyBalance> findByDate(LocalDate date);
}
