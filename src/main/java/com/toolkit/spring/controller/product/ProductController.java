package com.toolkit.spring.controller.product;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.toolkit.spring.controller.BaseController;
import com.toolkit.spring.model.product.Product;
import com.toolkit.spring.model.product.ProductCategory;
import com.toolkit.spring.model.product.ProductSubcategory;
import com.toolkit.spring.repository.product.ProductCategoryRepository;
import com.toolkit.spring.repository.product.ProductRepository;
import com.toolkit.spring.repository.product.ProductSubcategoryRepository;
import com.toolkit.spring.util.APIResponse;
import com.toolkit.spring.util.Data;
import com.toolkit.spring.util.response.ValidationErrorResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController
{
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    ProductSubcategoryRepository subcategories;

    @Autowired
    ProductCategoryRepository categories;

    @Autowired
    ProductRepository products;

    private static final Pageable pagination = Pageable.ofSize(10);

    @GetMapping("/page")
    public ModelAndView list(Model model)
    {
        model.addAttribute("product", new Product());
        model.addAttribute("active", "/product/page");
        loadOptions(model);

        return render("product/index").title("Produit").with(model);
    }

    public void loadOptions(Model model)
    {
        model.addAttribute("subcategories", Data.asMap(
            subcategories.findAll(), 
            ProductSubcategory::getId,
            ProductSubcategory::getLabel
        ));
    }

    @GetMapping
    public ResponseEntity<APIResponse> index(@RequestParam(name = "page", defaultValue = "0") Integer page)
    {
        if(page == 0)
        { return APIResponse.success(200, products.findAll()); }

        return APIResponse.success(200, Map.of(
            "page", products.findAll(pagination.withPage(page - 1)),
            "subcategories", Data.asMap(subcategories.findAll(), ProductSubcategory::getId, Function.identity()),
            "categories", Data.asMap(categories.findAll(), ProductCategory::getId, Function.identity())
        ));
    }    

    @PostMapping
    public ResponseEntity<APIResponse> store(@Valid Product product, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        products.save(product);
        return APIResponse.success(201, "Produit inséré avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable("id") Integer id, @Valid Product product, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        product.setId(id);
        products.save(product);
        
        return APIResponse.success(201, "Produit mis à jour avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable("id") Integer id)
    {
        products.deleteById(id);
        return APIResponse.success(201, "Produit supprimé avec succès");
    }
}
