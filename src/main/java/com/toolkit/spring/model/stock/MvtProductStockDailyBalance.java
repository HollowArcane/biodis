package com.toolkit.spring.model.stock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;

import com.toolkit.spring.model.product.Product;
import com.toolkit.spring.model.product.ProductCategory;
import com.toolkit.spring.model.product.ProductSubcategory;
import com.toolkit.spring.repository.product.ProductCategoryRepository;
import com.toolkit.spring.repository.product.ProductRepository;
import com.toolkit.spring.repository.product.ProductSubcategoryRepository;
import com.toolkit.spring.repository.stock.VMvtProductStockDailyBalanceRepository;

public class MvtProductStockDailyBalance
{
    public static class Record
    {
        private double entry;
        private double withdraw;

        public Record(double entry, double withdraw)
        {
            this.entry = entry;
            this.withdraw = withdraw;
        }

        public double getEntry()
        { return entry; }

        public double getWithdraw()
        { return withdraw; }

        public double getBalance()
        { return entry - withdraw; }
    }

    private List<ProductCategory> categories;

    // map category id to its subcategories
    private Map<Integer, List<ProductSubcategory>> subcategories;
    
    // map subcategory id to its products 
    private Map<Integer, List<Product>> products;

    // map subcategory id to its products 
    private Map<LocalDate, Map<Integer, Record>> records;

    private LocalDate reference;
    private int ndays;

    public MvtProductStockDailyBalance(
        @NonNull LocalDate reference,
        int ndays,
        @NonNull String idCategory,
        @NonNull ProductCategoryRepository categoriesRepo,
        @NonNull ProductSubcategoryRepository subcategoriesRepo,
        @NonNull ProductRepository productsRepo,
        @NonNull VMvtProductStockDailyBalanceRepository recordRepo
    )
    {
        this.ndays = ndays;
        this.reference = reference;

        if(idCategory.isEmpty())
        { this.categories = categoriesRepo.findAll(); }
        else
        {
            try
            { this.categories = categoriesRepo.findAllById(
                                    Arrays.asList(
                                        Integer.parseInt(idCategory))); }
            catch (Exception e)
            { e.printStackTrace(); }
        }
        this.subcategories = new HashMap<>();
        this.products = new HashMap<>();
        this.records = new LinkedHashMap<>();

        for(ProductCategory category: categories)
        {
            List<ProductSubcategory> subcategories = subcategoriesRepo.findAllByIdCategory(category.getId());
            this.subcategories.put(category.getId(), subcategories);
            
            for(ProductSubcategory subcategory: subcategories)
            { this.products.put(subcategory.getId(), productsRepo.findAllByIdSubcategory(subcategory.getId())); }
        }

        for(int i = 0; i < ndays; i++)
        {
            LocalDate date = reference.plusDays(-i);
            List<VMvtProductStockDailyBalance> records = recordRepo.findAllByDate(date);
            
            // map product to record
            HashMap<Integer, Record> map = new HashMap<>();

            for(VMvtProductStockDailyBalance record: records)
            { map.put(record.getIdProduct(), new Record(record.getQuantityEntry(), record.getQuantityWithdraw())); }

            this.records.put(date, map);
        }
    }

    public List<ProductCategory> getCategories()
    {
        return categories;
    }

    public Map<Integer, List<ProductSubcategory>> getSubcategories()
    {
        return subcategories;
    }

    public Map<Integer, List<Product>> getProducts()
    {
        return products;
    }

    public Map<LocalDate, Map<Integer, Record>> getRecords()
    {
        return records;
    }

    public LocalDate getReference() {
        return reference;
    }

    public int getNdays() {
        return ndays;
    }
}
