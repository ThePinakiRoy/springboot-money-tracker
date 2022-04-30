package com.codewithroy.resttester.expense.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/home")
    public String main(Model model) {
        model.addAttribute("appName", "Hello World post reload");
        return "home";
    }

    @GetMapping()
    public String auth() {
        return "auth";
    }

}
