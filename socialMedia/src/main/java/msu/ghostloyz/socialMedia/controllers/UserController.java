package msu.ghostloyz.socialMedia.controllers;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.model.User;
import msu.ghostloyz.socialMedia.services.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", User.builder().build());
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute User user) {
        if (!userService.createUser(user)) {
            return "registration";
        }
        return "redirect:/login";
    }

}
