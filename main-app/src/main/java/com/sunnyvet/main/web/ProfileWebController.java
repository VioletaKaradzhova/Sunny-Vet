package com.sunnyvet.main.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileWebController {

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }
}