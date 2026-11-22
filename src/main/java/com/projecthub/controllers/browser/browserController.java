package com.projecthub.controllers.browser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class browserController {

    @GetMapping("/browser")
    public String browser() {
        return "browser/index";
    }
}
