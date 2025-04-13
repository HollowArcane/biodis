package com.toolkit.spring.controller.shared;

import com.toolkit.spring.util.Renderer;

public abstract class BaseController
{    
    protected Renderer using(String layout)
    { return new Renderer().using(layout); }

    protected Renderer render(String page)
    { return new Renderer().render(page); }

    protected String redirect(String path)
    { return "redirect:" + path; }
}
