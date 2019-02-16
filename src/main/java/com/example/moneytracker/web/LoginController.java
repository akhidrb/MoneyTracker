package com.example.moneytracker.web;

import com.example.moneytracker.data.UserRepo;
import com.example.moneytracker.models.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    private final UserRepo userRepo;

    @Autowired
    public LoginController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (!isUserExist(username)) {
            return "redirect:/login";
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return "redirect:/home";
        } catch (Exception e) { // FirstFactorAuthenticationException
            return "redirect:/login";
        }
    }

    private Boolean isUserExist(String username) {
        User user = userRepo.findUserByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }


}
