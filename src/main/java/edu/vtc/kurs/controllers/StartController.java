package edu.vtc.kurs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {
    @GetMapping()
    public String getMenu() {
        return "start";
    }
}
