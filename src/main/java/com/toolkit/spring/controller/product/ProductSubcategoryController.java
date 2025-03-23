package com.toolkit.spring.controller.product;

import java.util.Map;

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

import com.toolkit.spring.controller.BaseController;
import com.toolkit.spring.model.table.product.ProductSubcategory;
import com.toolkit.spring.service.product.ProductCategoryService;
import com.toolkit.spring.service.product.ProductSubcategoryService;
import com.toolkit.spring.util.APIResponse;
import com.toolkit.spring.util.response.ValidationErrorResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product-subcategory")
public class ProductSubcategoryController extends BaseController
{
    @Autowired
    ProductSubcategoryService subcategories;

    @Autowired
    ProductCategoryService categories;

    private static final Pageable pagination = Pageable.ofSize(10);

    @GetMapping("/page")
    public ModelAndView list(Model model)
    {
        model.addAttribute("subcategory", new ProductSubcategory());
        model.addAttribute("active", "/product-subcategory/page");
        loadOptions(model);

        return render("product-subcategory/index").title("Sous-catégorie de Produit").with(model);
    }

    public void loadOptions(Model model)
    { model.addAttribute("categories", categories.options()); }

    @GetMapping
    public ResponseEntity<APIResponse> index(@RequestParam(name = "page", defaultValue = "0") Integer page)
    {
        if(page == 0)
        { return APIResponse.success(200, subcategories.findAll()); }

        return APIResponse.success(200, Map.of(
            "page", subcategories.findAll(pagination.withPage(page - 1)),
            "categories", categories.indexed()
        ));
    }    

    @PostMapping
    public ResponseEntity<APIResponse> store(@Valid ProductSubcategory category, BindingResult errors)
    {
        if(errors.hasErrors())
        { return APIResponse.error(400, new ValidationErrorResponse(errors)); } 

        subcategories.save(category);
        return APIResponse.success(201, "Sous-catégorie de Produit insérée avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable("id") Integer id, @Valid ProductSubcategory category, BindingResult errors)
    {
        if(errors.hasErrors())
        { return APIResponse.error(400, new ValidationErrorResponse(errors)); }

        category.setId(id);
        subcategories.save(category);
        
        return APIResponse.success(201, "Sous-catégorie de Produit mise à jour avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable("id") Integer id)
    {
        subcategories.deleteById(id);
        return APIResponse.success(201, "Sous-catégorie de Produit supprimée avec succès");
    }
}
