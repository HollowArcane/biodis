package com.toolkit.spring.model.dto.stock;

import java.time.LocalDate;
import java.util.Objects;

import com.toolkit.spring.model.dto.product.ProductDTO;
import com.toolkit.spring.model.view.product.VLabelProduct;

public class MvtProductStockDTO
{
    private final ProductDTO product;
    private final double quantityIn;
    private final double quantityOut;
    private final LocalDate date;

    private MvtProductStockDTO(ProductDTO product, double quantityIn, double quantityOut, LocalDate date)
    {
        this.product = product;
        this.quantityIn = quantityIn;
        this.quantityOut = quantityOut;
        this.date = date;
    }

    /**
     * @param entity cannot be null
     * @return a dto representing the given entity
     */
    public static MvtProductStockDTO fromEntity(VLabelProduct entity, LocalDate date)
    {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(date);

        return new MvtProductStockDTO(
            ProductDTO.fromEntity(entity),
            entity.getQuantityIn(),
            entity.getQuantityOut(),
            date
        );
    }
    
    public ProductDTO getProduct()
    { return product; }

    public double getQuantityIn()
    { return quantityIn; }

    public double getQuantityOut()
    { return quantityOut; }

    public LocalDate getDate()
    { return date; }

    public double getBalance()
    { return getQuantityIn() - getQuantityOut(); }
}
