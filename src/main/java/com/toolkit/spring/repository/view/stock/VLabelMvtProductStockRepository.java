package com.toolkit.spring.repository.view.stock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toolkit.spring.model.view.stock.VLabelMvtProductStock;

public interface VLabelMvtProductStockRepository extends JpaRepository<VLabelMvtProductStock, Integer>
{
    /**
     * @param dateMin
     * @param dateMax
     * @return mouvement records ordered by date asc to allow
     * linear accumulation of balance
     */
    @Query(nativeQuery = true, value = "SELECT * FROM v_label_mvt_product_stock WHERE date > :dateMin AND date <= :dateMax ORDER BY date ASC")
    List<VLabelMvtProductStock> findByDateMinDateMax(LocalDate dateMin, LocalDate dateMax);

    @Query(nativeQuery = true, value = "SELECT * FROM v_label_mvt_product_stock WHERE date < :dateMax AND (:idProduct IS NULL OR id_product = :idProduct)AND (:idSubcategory IS NULL OR id_product_subcategory = :idSubcategory)AND (:idCategory IS NULL OR id_product_category = :idCategory) ORDER BY date ASC")
    List<VLabelMvtProductStock> findByDateMaxAndIdProduct(LocalDate dateMax, Integer idProduct, Integer idSubcategory, Integer idCategory);

    @Query(nativeQuery = true, value = "SELECT * FROM v_label_mvt_product_stock WHERE date >= :dateMin AND date < :dateMax AND (:idProduct IS NULL OR id_product = :idProduct)AND (:idSubcategory IS NULL OR id_product_subcategory = :idSubcategory)AND (:idCategory IS NULL OR id_product_category = :idCategory) ORDER BY date ASC")
    List<VLabelMvtProductStock> findByDateMaxAndIdProduct(LocalDate dateMin, LocalDate dateMax, Integer idProduct, Integer idSubcategory, Integer idCategory);

    @Query(nativeQuery = true, value = "SELECT DISTINCT date FROM v_label_mvt_product_stock WHERE date < :dateMax ORDER BY date DESC LIMIT :sampleCount")
    LocalDate[] findLatestSampleDate(LocalDate dateMax, int sampleCount);
}
