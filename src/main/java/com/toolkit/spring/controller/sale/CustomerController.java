package com.toolkit.spring.controller.sale;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.toolkit.spring.controller.shared.BaseController;
import com.toolkit.spring.model.table.sale.Customer;
import com.toolkit.spring.service.sale.CustomerService;
import com.toolkit.spring.util.APIResponse;
import com.toolkit.spring.util.response.ValidationErrorResponse;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/sale/customer")
public class CustomerController extends BaseController
{
    @Autowired
    CustomerService customers;

    private static final Pageable pagination = Pageable.ofSize(10);

    @GetMapping("/page")
    public ModelAndView list(Model model)
    {
        model.addAttribute("customer", new Customer());
        model.addAttribute("active", "/sale/customer/page");

        return render("sale/customer/index").title("Client").with(model);
    }

    @GetMapping
    public ResponseEntity<APIResponse> index(@RequestParam(name = "page", defaultValue = "0") Integer page)
    {
        if(page == 0)
        { return APIResponse.success(200, customers.findAll()); }

        return APIResponse.success(200, Map.of(
            "page", customers.findAll(pagination.withPage(page - 1))
        ));
    }

    @PostMapping
    public ResponseEntity<APIResponse> store(@Valid Customer customer, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        customers.save(customer);
        return APIResponse.success(201, "Client inséré avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable("id") Integer id, @Valid Customer customer, BindingResult errors)
    {
        if(errors.hasErrors())
        {
            return APIResponse.error(400, new ValidationErrorResponse(errors));
        }

        customer.setId(id);
        customers.save(customer);
        
        return APIResponse.success(201, "Client mis à jour avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> delete(@PathVariable("id") Integer id)
    {
        customers.deleteById(id);
        return APIResponse.success(201, "Client supprimé avec succès");
    }
}
