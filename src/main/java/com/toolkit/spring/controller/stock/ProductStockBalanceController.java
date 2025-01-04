package com.toolkit.spring.controller.stock;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.toolkit.spring.controller.BaseController;
import com.toolkit.spring.model.product.Product;
import com.toolkit.spring.model.product.ProductCategory;
import com.toolkit.spring.model.product.ProductSubcategory;
import com.toolkit.spring.model.stock.MvtProductStockDailyBalance;
import com.toolkit.spring.repository.product.ProductCategoryRepository;
import com.toolkit.spring.repository.product.ProductRepository;
import com.toolkit.spring.repository.product.ProductSubcategoryRepository;
import com.toolkit.spring.repository.stock.VMvtProductStockDailyBalanceRepository;
import com.toolkit.spring.service.stock.ProductStockDailyBalancePDFService;
import com.toolkit.spring.util.APIResponse;
import com.toolkit.spring.util.Data;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/stock-balance")
public class ProductStockBalanceController extends BaseController
{
    @Autowired
    ProductCategoryRepository categories;

    @Autowired
    ProductSubcategoryRepository subcategories;

    @Autowired
    ProductRepository products;

    @Autowired
    VMvtProductStockDailyBalanceRepository records;

    @Autowired
    ProductStockDailyBalancePDFService pdf;

    @GetMapping("/page")
    public ModelAndView list(Model model)
    {
        model.addAttribute("today", LocalDate.now().toString());
        
        Map<String, String> data = new HashMap<>(Data.asMap(
            categories.findAll(),
            e -> e.getId().toString(),
            ProductCategory::getLabel
        ));
        data.put("", "Tous");
        

        model.addAttribute("categories", data);
        model.addAttribute("active", "/stock-balance/page");

        return render("stock-balance/index").title("Bilan de Stock").with(model);
    }

    @GetMapping
    public ResponseEntity<APIResponse> index(
        @RequestParam(name = "date", required = false) String dateStr,
        @RequestParam(name = "ndays", defaultValue = "7") Integer ndays,
        @RequestParam(name = "idCategory", defaultValue = "") String idCategory
    )
    {
        LocalDate date;
        if(dateStr.isEmpty())
        { date = LocalDate.now(); }
        else
        {
            try
            { date = LocalDate.parse(dateStr); }
            catch (DateTimeParseException e)
            { return APIResponse.error(400, e.getMessage()); }
        }

        MvtProductStockDailyBalance balance = new MvtProductStockDailyBalance(date, ndays, idCategory, categories, subcategories, products, records);
        return APIResponse.success(200, balance);
    }

    @GetMapping("/pdf")
    public void pdf(
        @RequestParam(name = "date", required = false) String dateStr,
        @RequestParam(name = "ndays", defaultValue = "7") Integer ndays,
        @RequestParam(name = "idCategory", defaultValue = "") String idCategory,
        HttpServletResponse response,
        Model model
    )
    {
        LocalDate date;
        if(dateStr.isEmpty())
        { date = LocalDate.now(); }
        else
        {
            try
            { date = LocalDate.parse(dateStr); }
            catch (DateTimeParseException e)
            {
                response.setStatus(400);
                return;
            }
        }

        MvtProductStockDailyBalance balance = new MvtProductStockDailyBalance(date, ndays, idCategory, categories, subcategories, products, records);
        pdf.create("/pages/stock-balance/pdf", model, response, balance);
    }
}
