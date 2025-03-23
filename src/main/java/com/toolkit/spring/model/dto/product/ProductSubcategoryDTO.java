package com.toolkit.spring.model.dto.product;

import java.util.Objects;

import com.toolkit.spring.model.view.product.VLabelProduct;

public class ProductSubcategoryDTO implements Comparable<ProductSubcategoryDTO>
{
    private final Integer id;
    private final String label;
    private final ProductCategoryDTO category;

    private ProductSubcategoryDTO(Integer id, String label, ProductCategoryDTO category)
    {
        this.id = id;
        this.label = label;
        this.category = category;
    }

    public static ProductSubcategoryDTO fromEntity(VLabelProduct stock)
    {
        Objects.requireNonNull(stock);
        return new ProductSubcategoryDTO(
            stock.getIdProductSubcategory(),
            stock.getProductSubcategory(),
            ProductCategoryDTO.fromEntity(stock)
        );
    }

    public Integer getId()
    { return id; }

    public String getLabel()
    { return label; }

    public ProductCategoryDTO getCategory()
    { return category; }

    @Override
    public int compareTo(ProductSubcategoryDTO o)
    { return Integer.compare(id, o.id); }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
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
        ProductSubcategoryDTO other = (ProductSubcategoryDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }
    
    @Override
    /*
     * ATTENTION!: This method is used to turn the instance of this dto to a string
     * during JSON serialization. If you want to blame anyone, blame spring for
     * being stupid
     */
    public String toString()
    { return label; }
}
