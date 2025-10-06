package com.railpass.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GlobalController {
    @GetMapping("/assets/{*path}") 
    public String assets(@PathVariable String path) {
        return "forward:/static/assets/" + path;
    }

    @GetMapping("/favicon.ico")
    public String favicon() {
        return "forward:/static/favicon.ico";
    }

    @GetMapping("/")
    public String home() {
        return "forward:/static/index.html";
    }

    @GetMapping("/robots.txt")
    public String robots() {
        return "forward:/static/robots.txt";
    }
}
