package com.example.security.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, HttpSession httpSession) {
        model.addAttribute("name", httpSession.getAttribute("name"));
        return "home";
    }

    @GetMapping("/admin")
    public ModelAndView admin(@AuthenticationPrincipal UserDetails principal) {
        ModelAndView mav = new ModelAndView("admin");
        mav.addObject("user", principal);
        return mav;
    }

    @GetMapping("/user")
    public String user(Principal principal, Model model) {
        model.addAttribute("user", principal);
        return "user";
    }
}
