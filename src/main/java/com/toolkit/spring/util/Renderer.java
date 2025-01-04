package com.toolkit.spring.util;

import java.util.Objects;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public class Renderer
{
    private static final String PAGE_ROOT = "/pages/";
    private static final String LAYOUT_ROOT = "/layouts/";

    private static final String DEFAULT_LAYOUT = "main";

    private String layout;
    private String page;
    private String title;

    public Renderer()
    {
        this.layout = DEFAULT_LAYOUT;
        this.title = "<insert-title-here>";
    }

    public Renderer using(String layout)
    {
        Objects.requireNonNull(layout);
        this.layout = layout;
        return this;
    }

    public Renderer render(String page)
    {
        Objects.requireNonNull(page);
        this.page = page;
        return this;
    }

    public Renderer title(String title)
    {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }

    public ModelAndView with(Model model)
    {
        Objects.requireNonNull(model);
        model.addAttribute("page", PAGE_ROOT + page);
        model.addAttribute("title", title);
        return new ModelAndView(LAYOUT_ROOT + layout);
    }
}
