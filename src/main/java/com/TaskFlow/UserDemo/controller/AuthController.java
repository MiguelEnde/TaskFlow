package com.taskflow.controller;

import com.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Principal principal,
                            Model model) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        if (error != null) {
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Has cerrado sesión correctamente.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Principal principal) {
        if (principal != null) return "redirect:/dashboard";
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String displayName,
                                   @RequestParam String email,
                                   RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(username, password, displayName, email);
            redirectAttributes.addFlashAttribute("successMsg", "¡Cuenta creada! Inicia sesión.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
