package com.toolkit.spring.model.dto.product;

import java.util.Objects;

import com.toolkit.spring.model.view.product.VLabelProduct;

public class ProductDTO implements Comparable<ProductDTO>
{
    private final Integer id;
    private final String label;
    private final ProductSubcategoryDTO subcategory;
    private final double thresholdWarning;

    private ProductDTO(Integer id, String label, ProductSubcategoryDTO subcategory, double thresholdWarning)
    {
        this.id = id;
        this.label = label;
        this.subcategory = subcategory;
        this.thresholdWarning = thresholdWarning;
    }    

    /**
     * @param entity cannot be null
     * @return
     */
    public static ProductDTO fromEntity(VLabelProduct entity)
    {
        Objects.requireNonNull(entity);

        return new ProductDTO(
            entity.getId(),
            entity.getProduct(),
            ProductSubcategoryDTO.fromEntity(entity),
            entity.getThresholdWarning()
        );
    }

    public Integer getId()
    { return id; }

    public String getLabel()
    { return label; }

    public ProductSubcategoryDTO getSubcategory()
    { return subcategory; }

    public double getThresholdWarning()
    { return thresholdWarning; }

    
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
        ProductDTO other = (ProductDTO) obj;
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
    public int compareTo(ProductDTO o)
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
