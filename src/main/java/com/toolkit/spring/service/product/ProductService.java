package com.toolkit.spring.service.product;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.toolkit.spring.model.dto.stock.MvtProductStockDTO;
import com.toolkit.spring.model.table.product.Product;
import com.toolkit.spring.model.view.product.VLabelProduct;
import com.toolkit.spring.model.view.stock.VLabelMvtProductStock;
import com.toolkit.spring.repository.table.product.ProductRepository;
import com.toolkit.spring.repository.view.product.VLabelProductRepository;
import com.toolkit.spring.repository.view.stock.VLabelMvtProductStockRepository;
import com.toolkit.spring.service.interfaces.OperationService;
import com.toolkit.spring.service.interfaces.OptionService;
import com.toolkit.spring.service.interfaces.ReadService;
import com.toolkit.spring.util.Groups;

@Service
public class ProductService implements
    ReadService<VLabelProduct, Integer>,
    OptionService<VLabelProduct, Integer>,
    OperationService<Product, Integer>
{
    public static enum ChartGroup
    {
        PRODUCT, CATEGORY, SUBCATEGORY;

        public static ChartGroup parseOrDefault(Optional<String> group)
        {
            Objects.requireNonNull(group);
            return group.map(g -> {
                switch (g.toLowerCase()) {
                    case "product":
                        return PRODUCT;
                    case "category":
                        return CATEGORY;
                    default:
                        return SUBCATEGORY;
                }
            }).orElse(SUBCATEGORY);
        }
    }


    @Autowired
    ProductRepository createRepository;

    @Autowired
    VLabelMvtProductStockRepository stocks;

    @Autowired
    VLabelProductRepository readRepository;

    @Override
    public VLabelProductRepository getReadRepository()
    { return readRepository; }

    @Override
    public JpaRepository<Product, Integer> getOperationRepository()
    { return createRepository; }
    
    /**
     * @param dates: days on which balance will be computed on (inclusive).
     * @param idProductCategory
     * @return balance data grouped by date, category, subcategory and product
     */
    public Map<LocalDate, Object> getTableTreeData(LocalDate[] dates, Optional<Integer> idProductCategory, Optional<Integer> idProductSubcategory)
    {
        Objects.requireNonNull(dates);
        Objects.requireNonNull(idProductCategory);
        Objects.requireNonNull(idProductSubcategory);

        // offset by one day because query is exclusive
        for(int i = 0; i < dates.length; i++)
        { dates[i] = dates[i].plusDays(1); }

        Map<LocalDate, Object> result = new HashMap<>();
        List<VLabelProduct> productsThisDay = readRepository.findAllByCriteria(
            idProductCategory.orElse(null), idProductSubcategory.orElse(null)
        );

        for(LocalDate thisDay: dates)
        {
            List<MvtProductStockDTO> list = productsThisDay.stream().map(
                product -> MvtProductStockDTO.fromEntity(product, thisDay)
            ).toList();

            var data = Groups.group(list, e0 -> e0.getProduct().getSubcategory().getCategory())
                .then(group1 -> Groups.group(group1, e1 -> e1.getProduct().getSubcategory())
                    .then(group2 -> Groups.group(group2, e2 -> e2.getProduct())));
            
            result.put(thisDay, data);
        }
        return result;
    }

    /**
     * @param dates: list of date on which balance will be computed (exclusive)
     * @param idProductCategory
     * @param idProductCategory
     * @param accumulate
     * @param group
     * @return balance data grouped by product and date
     */
    public Map<String, Map<LocalDate, Double>> getChartData(
        LocalDate[] dates,
        Optional<Integer> idProductCategory,
        Optional<Integer> idProductSubcategory,
        boolean accumulate,
        ChartGroup group
    )
    {
        Objects.requireNonNull(dates);
        Objects.requireNonNull(idProductCategory);
        Objects.requireNonNull(idProductSubcategory);
        Objects.requireNonNull(group);

        Map<String, Map<LocalDate, Double>> result = new HashMap<>();
        List<VLabelProduct> productsThisDay = readRepository.findAllByCriteria(
            idProductCategory.orElse(null), idProductSubcategory.orElse(null)
        );

        if(accumulate)
        {
            for(LocalDate thisDay: dates)
            {
                Objects.requireNonNull(thisDay);
                getProductsThisDay(productsThisDay, thisDay, group);
    
                for(VLabelProduct product: productsThisDay)
                {
                    Map<LocalDate, Double> map = Map.of();
                    switch (group) {
                        case PRODUCT:
                            map = result.computeIfAbsent(product.getProduct(), key -> new HashMap<>());
                            break;
                        case SUBCATEGORY:
                            map = result.computeIfAbsent(product.getProductSubcategory(), key -> new HashMap<>());
                            break;
                        case CATEGORY:
                            map = result.computeIfAbsent(product.getProductCategory(), key -> new HashMap<>());
                            break;
                    };;
                    map.put(thisDay, product.getQuantityIn() - product.getQuantityOut());
                }
            }
        }
        else
        {
            for(int i = 1; i < dates.length; i++)
            {   
                Objects.requireNonNull(dates[i - 1]);
                Objects.requireNonNull(dates[i]);
    
                getProductsThisDay(productsThisDay, dates[i - 1], dates[i], group);
    
                for(VLabelProduct product: productsThisDay)
                {
                    Map<LocalDate, Double> map = Map.of();
                    switch (group) {
                        case PRODUCT:
                            map = result.computeIfAbsent(product.getProduct(), key -> new HashMap<>());
                            break;
                        case SUBCATEGORY:
                            map = result.computeIfAbsent(product.getProductSubcategory(), key -> new HashMap<>());
                            break;
                        case CATEGORY:
                            map = result.computeIfAbsent(product.getProductCategory(), key -> new HashMap<>());
                            break;
                    };;
                    map.put(dates[i], product.getQuantityIn() - product.getQuantityOut());
                }
            }
        }

        return result;
    }

    /**
     * @param productsThisDay
     * @param thisDay
     * @param group by which data will be grouped
     * 
     * ATTENTION!: Use this method with extreme precaution as it changes data directly
     * on the given list of entity, note that since the given entity represents a view,
     * it should not cause any issue inside the database
     * 
     * This method compute the balance of each of the given product at the given date
     * taking into account all mouvement in the past
     */
    private void getProductsThisDay(List<VLabelProduct> productsThisDay, LocalDate thisDay, ChartGroup group)
    {           
        for(VLabelProduct product: productsThisDay)
        {
            List<VLabelMvtProductStock> data = List.of();
            switch (group) {
                case PRODUCT:
                    data = stocks.findByDateMaxAndIdProduct(thisDay, product.getId(), null, null);
                    break;
                case SUBCATEGORY:
                    data = stocks.findByDateMaxAndIdProduct(thisDay, null, product.getIdProductSubcategory(), null);
                    break;
                case CATEGORY:
                    data = stocks.findByDateMaxAndIdProduct(thisDay, null, null, product.getIdProductCategory());
                    break;
            };

            double quantityIn = data.stream().collect(
                Collectors.summingDouble(VLabelMvtProductStock::getQuantityIn)
            );
            double quantityOut = data.stream().collect(
                Collectors.summingDouble(VLabelMvtProductStock::getQuantityOut)
            );

            product.setQuantityIn(quantityIn);
            product.setQuantityOut(quantityOut);
        }
    }

    /**
     * @param productsThisDay
     * @param dateMin
     * @param dateMax
     * @param group
     * 
     * ATTENTION!: Use this method with extreme precaution as it changes data directly
     * on the given list of entity, note that since the given entity represents a view,
     * it should not cause any issue inside the database
     * 
     * This method compute the balance of each of the given product at the given date
     * taking into account all mouvement in the past
     */
    private void getProductsThisDay(List<VLabelProduct> productsThisDay, LocalDate dateMin, LocalDate dateMax, ChartGroup group)
    {           
        for(VLabelProduct product: productsThisDay)
        {
            List<VLabelMvtProductStock> data = List.of();
            switch (group) {
                case PRODUCT:
                    data = stocks.findByDateMaxAndIdProduct(dateMin, dateMax, product.getId(), null, null);
                    break;
                case SUBCATEGORY:
                    data = stocks.findByDateMaxAndIdProduct(dateMin, dateMax, null, product.getIdProductSubcategory(), null);
                    break;
                case CATEGORY:
                    data = stocks.findByDateMaxAndIdProduct(dateMin, dateMax, null, null, product.getIdProductCategory());
                    break;
            };
            double quantityIn = data.stream().collect(
                Collectors.summingDouble(VLabelMvtProductStock::getQuantityIn)
            );
            double quantityOut = data.stream().collect(
                Collectors.summingDouble(VLabelMvtProductStock::getQuantityOut)
            );

            product.setQuantityIn(quantityIn);
            product.setQuantityOut(quantityOut);
        }
    }
}
