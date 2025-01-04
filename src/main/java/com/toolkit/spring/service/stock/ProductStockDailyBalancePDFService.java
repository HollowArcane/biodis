package com.toolkit.spring.service.stock;


import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.toolkit.spring.model.product.Product;
import com.toolkit.spring.model.product.ProductCategory;
import com.toolkit.spring.model.product.ProductSubcategory;
import com.toolkit.spring.model.stock.MvtProductStockDailyBalance;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ProductStockDailyBalancePDFService
{
    private final SpringTemplateEngine templateEngine;

    public ProductStockDailyBalancePDFService(SpringTemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }

    public void create(String template, Model model, HttpServletResponse response, MvtProductStockDailyBalance balance)
    {
        try
        {
            model.addAttribute("balance", balance);
        
        String[][] colors = {
            { "#3F51B5", "#FF8F00", "#1976D2", "#558B2F", "#0097A7", "#5D4037", "#00897B" },
            { "#F6F6F6", "#FFFFFF" },
            { "#FFEBEE", "#EDE7F6", "#E3F2FD", "#E0F2F1", "#FFF3E0", "#EFEBE9", "##CEFF1", "#FCE4EC" },
        };

        Map<Integer, Object> categoryProps = new HashMap<>();
        Map<Integer, Object> subcategoryProps = new HashMap<>();
        Map<Integer, Object> productProps = new HashMap<>();
        int cursor1 = 0, cursor2 = 0, cursor3 = 0;

        
        for(ProductCategory category: balance.getCategories())
        {
            cursor1 = (cursor1 + 1) % colors[0].length;
            int lengthStack = 0;

            for(ProductSubcategory subcategory: balance.getSubcategories().get(category.getId()))
            {
                cursor2 = (cursor2 + 1) % colors[1].length;
                int c2 = cursor2;
                subcategoryProps.put(subcategory.getId(), new Object() {
                    public String color = colors[1][c2];
                    public int length = balance.getProducts().get(subcategory.getId()).size() * 3;
                });
                lengthStack += balance.getProducts().get(subcategory.getId()).size() * 3;
                
                for(Product product: balance.getProducts().get(subcategory.getId()))
                {
                    cursor3 = (cursor3 + 1) % colors[2].length;
                    int c3 = cursor3;
                    productProps.put(product.getId(), new Object() {
                        public String color = colors[2][c3];
                        public int length = 3;
                    });
                }
            }

            int c1 = cursor1;
            int l = lengthStack;
            categoryProps.put(category.getId(), new Object() {
                public String color = colors[0][c1];
                public int length = l;
            });
        }

        model.addAttribute("categoryProps", categoryProps);
        model.addAttribute("subcategoryProps", subcategoryProps);
        model.addAttribute("productProps", productProps);

            Context context = new Context();
            model.asMap().forEach(context::setVariable);
    
            String htmlContent = templateEngine.process(template, context);
    
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=stock-balance.pdf");
    
            try (OutputStream os = response.getOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
    
                renderer.setDocumentFromString(htmlContent);
                renderer.layout();
                renderer.createPDF(os, false);
                renderer.finishPDF();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }    
}