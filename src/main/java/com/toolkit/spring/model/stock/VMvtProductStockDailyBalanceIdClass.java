package com.toolkit.spring.model.stock;

import java.io.Serializable;
import java.time.LocalDate;

public class VMvtProductStockDailyBalanceIdClass implements Serializable
{
    private int idProduct;
    private LocalDate date;

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + idProduct;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VMvtProductStockDailyBalanceIdClass other = (VMvtProductStockDailyBalanceIdClass) obj;
        if (idProduct != other.idProduct)
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }

    
}