package com.toolkit.spring.controller.product;

import java.util.List;
import java.util.Map;

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
import com.toolkit.spring.model.product.ProductCategory;
import com.toolkit.spring.repository.product.ProductCategoryRepository;
import com.toolkit.spring.util.APIResponse;
import com.toolkit.spring.util.response.ValidationErrorResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController extends BaseController
{
    @Autowired
    ProductCategoryRepository categories;

    private static final Pageable pagination = Pageable.ofSize(10);

    @GetMapping("/page")
    public ModelAndView list(Model model)
    {
        model.addAttribute("category", new ProductCategory());
        model.addAttribute("active", "/product-category/page");

        return render("product-category/index").title("Catégorie de Produit").with(model);
    }

    @GetMapping
    public ResponseEntity<APIResponse> index(@RequestParam(name = "page", defaultValue = "0") Integer page)
    {
        if(page == 0)
        { return APIResponse.success(200, categories.findAll()); }

        return APIResponse.success(200, Map.of(
            "page", categories.findAll(pagination.withPage(page - 1))
        ));
    }

    @PostMapping
    public ResponseEntity<APIResponse> store(@Valid ProductCategory category, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        categories.save(category);
        return APIResponse.success(201, "Catégorie de Produit insérée avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable("id") Integer id, @Valid ProductCategory category, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        category.setId(id);
        categories.save(category);
        
        return APIResponse.success(201, "Catégorie de Produit mise à jour avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable("id") Integer id)
    {
        categories.deleteById(id);
        return APIResponse.success(201, "Catégorie de Produit supprimée avec succès");
    }
}
