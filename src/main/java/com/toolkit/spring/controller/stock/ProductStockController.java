package com.toolkit.spring.controller.stock;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.toolkit.spring.controller.BaseController;
import com.toolkit.spring.model.table.stock.MvtProductStock;
import com.toolkit.spring.service.product.ProductCategoryService;
import com.toolkit.spring.service.product.ProductService;
import com.toolkit.spring.service.product.ProductSubcategoryService;
import com.toolkit.spring.service.stock.MvtProductStockService;
import com.toolkit.spring.util.APIResponse;
import com.toolkit.spring.util.DateRange;
import com.toolkit.spring.util.response.ValidationErrorResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/stock")
public class ProductStockController extends BaseController
{
    @Autowired
    MvtProductStockService stocks;

    @Autowired
    ProductService products;

    @Autowired
    ProductCategoryService categories;

    @Autowired
    ProductSubcategoryService subcategories;

    private static final Pageable pagination = Pageable.ofSize(10);

    private final SpringTemplateEngine templateEngine;

    public ProductStockController(SpringTemplateEngine templateEngine)
    { this.templateEngine = templateEngine; } 

    @GetMapping("/page")
    public ModelAndView list(Model model)
    {
        model.addAttribute("productStock", new MvtProductStock());
        loadOptions(model);
        model.addAttribute("active", "/stock/page");

        return render("stock/index").title("Mouvement de Stock").with(model);
    }

    @GetMapping
    public ResponseEntity<APIResponse> index(@RequestParam(name = "page") Integer page)
    {
        return APIResponse.success(200, Map.of(
            "page", stocks.findAll(pagination.withPage(page - 1)),
            "categories", categories.findAll(),
            "subcategories", subcategories.findAll(),
            "products", products.findAll()
        ));
    }
    
    public void loadOptions(Model model)
    {
        model.addAttribute("products", products.options());
        model.addAttribute("categories", categories.options());
        model.addAttribute("subcategories", subcategories.options());
    }

    @PostMapping
    public ResponseEntity<APIResponse> store(@Valid MvtProductStock productStock, BindingResult errors)
    {
        if(errors.hasErrors())
        { return APIResponse.error(400, new ValidationErrorResponse(errors)); }

        stocks.save(productStock);
        return APIResponse.success(201, "Stock de Produit inséré avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable("id") Integer id, @Valid MvtProductStock productStock, BindingResult errors)
    {
        if(errors.hasErrors())
        { return APIResponse.error(400, new ValidationErrorResponse(errors)); }

        productStock.setId(id);
        stocks.save(productStock);
        
        return APIResponse.success(201, "Stock de Produit mis à jour avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable("id") Integer id)
    {
        stocks.deleteById(id);
        return APIResponse.success(201, "Stock de Produit supprimé avec succès");
    }

    @GetMapping("/chart/page")
    public ModelAndView chart(Model model)
    {
        model.addAttribute("categories", categories.options());
        model.addAttribute("today", LocalDate.now());
        return render("stock/chart").title("Graphique d'Évolution du Stock")
            .with(model);
    }

    @GetMapping("/chart")
    public ResponseEntity<APIResponse> chart(
        @RequestParam(name = "date", required = false) LocalDate date,
        @RequestParam(name = "nSamples", defaultValue = "12") int nSamples,
        @RequestParam(name = "idCategory") Optional<Integer> idCategory,
        @RequestParam(name = "sampleType") String sampleType,
        @RequestParam(name = "accumulate") String accumulate
    )
    {
        if(date == null)
        { date = LocalDate.now(); }
        if(nSamples <= 0)
        { nSamples = 1; }

        boolean acc = "accumulate".equals(accumulate);
        if(acc)
        { nSamples++; }

        LocalDate[] dates = new LocalDate[0];
        if("week".equals(sampleType))
        { dates = DateRange.weeksAsc(date, nSamples); }
        else if("day".equals(sampleType))
        { dates = DateRange.daysAsc(date, nSamples); }
        else // expects ("month".equals(sampleType))
        { dates = DateRange.monthsAsc(date, nSamples); }

        Map<String, Map<LocalDate, Double>> chart = products.getChartData(dates, idCategory, acc);
    
        return APIResponse.success(200, Map.of(
            "data", chart,
            "dates", dates
        ));
    }

    @GetMapping("/balance/page")
    public ModelAndView balance(Model model)
    {
        model.addAttribute("categories", categories.options());
        model.addAttribute("active", "/stock/balance/page");
        model.addAttribute("today", LocalDate.now());

        return render("stock/balance").title("Bilan Journalier du Stock").with(model);
    }

    @GetMapping("/balance")
    public ResponseEntity<APIResponse> balance(
        @RequestParam(name = "date", required = false) LocalDate date,
        @RequestParam(name = "ndays", defaultValue = "7") int ndays,
        @RequestParam(name = "idCategory") Optional<Integer> idCategory
    )
    {
        if(date == null)
        { date = LocalDate.now(); }
        if(ndays <= 0)
        { ndays = 1; }

        LocalDate[] dates = stocks.findLatestSampleDate(date, ndays);
        return APIResponse.success(200, Map.of(
            "data", products.getTableTreeData(dates, idCategory),
            "dates", dates
        ));
    }


    @GetMapping("/pdf")
    public void pdf(
        @RequestParam(name = "date", required = false) LocalDate date,
        @RequestParam(name = "ndays", defaultValue = "7") int ndays,
        @RequestParam(name = "idCategory") Optional<Integer> idCategory,
        HttpServletResponse response,
        Model model
    )
    {
        if(date == null)
        { date = LocalDate.now(); }
        if(ndays <= 0)
        { ndays = 1; }

        try
        {    
            String[] COLORS_1 = {
                "#3F51B5", "#FF8F00", "#1976D2", "#558B2F", "#0097A7", "#5D4037", "#00897B"
            };
        
            String[] COLORS_2 = {
                "#F6F6F6", "#FFFFFF"
            };
        
            String[] COLORS_3 = {
                "#FFEBEE", "#EDE7F6", "#E3F2FD", "#E0F2F1", "#FFF3E0", "#EFEBE9", "##CEFF1", "#FCE4EC"
            };

            LocalDate[] dates = stocks.findLatestSampleDate(date, ndays);
            model.addAttribute("data", products.getTableTreeData(dates, idCategory));
            model.addAttribute("ndays", ndays);
            model.addAttribute("today", date);
            model.addAttribute("dates", dates);
            model.addAttribute("COLORS_1", COLORS_1);
            model.addAttribute("COLORS_2", COLORS_2);
            model.addAttribute("COLORS_3", COLORS_3);
        
            Context context = new Context();
            model.asMap().forEach(context::setVariable);
    
            String htmlContent = templateEngine.process("/pages/stock/pdf", context);
            // System.out.println(htmlContent);

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
