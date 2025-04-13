package com.toolkit.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.toolkit.spring.controller.shared.BaseController;

@Controller
public class IndexController extends BaseController
{
    @GetMapping
    public String index()
    { return redirect("/stock/mvt-product-stock/dashboard/page"); }
}
