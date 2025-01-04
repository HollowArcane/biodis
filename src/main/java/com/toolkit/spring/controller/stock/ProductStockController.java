package com.toolkit.spring.controller.stock;

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
import com.toolkit.spring.model.product.Product;
import com.toolkit.spring.model.stock.MvtProductStock;
import com.toolkit.spring.model.stock.Action;
import com.toolkit.spring.repository.product.ProductRepository;
import com.toolkit.spring.repository.stock.MvtProductStockRepository;
import com.toolkit.spring.repository.stock.VLabelMvtProductStockRepository;
import com.toolkit.spring.util.APIResponse;
import com.toolkit.spring.util.Data;
import com.toolkit.spring.util.response.ValidationErrorResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/stock")
public class ProductStockController extends BaseController
{
    @Autowired
    MvtProductStockRepository CRUDRepository;

    @Autowired
    VLabelMvtProductStockRepository repository;

    @Autowired
    ProductRepository products;

    private static final Pageable pagination = Pageable.ofSize(10);

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
        return APIResponse.success(200, repository.findAll(pagination.withPage(page - 1)));
    }
    
    public void loadOptions(Model model)
    {
        model.addAttribute("products", Data.asMap(
            products.findAll(), 
            Product::getId,
            Product::getLabel
        ));
        model.addAttribute("actions", Data.asMap(
            Action.all(), 
            Action::getId,
            Action::getLabel
        ));
    }

    @PostMapping
    public ResponseEntity<APIResponse> store(@Valid MvtProductStock productStock, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        CRUDRepository.save(productStock);
        return APIResponse.success(201, "Stock de Produit inséré avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable("id") Integer id, @Valid MvtProductStock productStock, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        productStock.setId(id);
        CRUDRepository.save(productStock);
        
        return APIResponse.success(201, "Stock de Produit mis à jour avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable("id") Integer id)
    {
        CRUDRepository.deleteById(id);
        return APIResponse.success(201, "Stock de Produit supprimé avec succès");
    }
}
