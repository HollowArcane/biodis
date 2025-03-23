package com.toolkit.spring.model.dto.product;

import java.util.Objects;

import com.toolkit.spring.model.view.product.VLabelProduct;

public class ProductCategoryDTO implements Comparable<ProductCategoryDTO>
{
    private final Integer id;
    private final String label;

    private ProductCategoryDTO(Integer id, String label)
    {
        this.id = id;
        this.label = label;
    }

    public static ProductCategoryDTO fromEntity(VLabelProduct entity)
    {
        Objects.requireNonNull(entity);
        return new ProductCategoryDTO(
            entity.getIdProductCategory(),
            entity.getProductCategory()
        );
    }

    public Integer getId()
    { return id; }

    public String getLabel()
    { return label; }

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
        ProductCategoryDTO other = (ProductCategoryDTO) obj;
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
    public int compareTo(ProductCategoryDTO o)
    { return Integer.compare(id, o.id); }

    @Override
    /*
     * ATTENTION!: This method is used to turn the instance of this dto to a string
     * during JSON serialization. If you want to blame anyone, blame spring for
     * being stupid
     */
    public String toString()
    { return label; }
}
