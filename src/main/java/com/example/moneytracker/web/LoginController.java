package com.example.moneytracker.web;

import com.example.moneytracker.data.UserRepo;
import com.example.moneytracker.models.User;
import com.example.moneytracker.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class LoginController {

    private final UserRepo userRepo;
    private final SessionUtils sessionUtils;

    @Autowired
    public LoginController(UserRepo userRepo, SessionUtils sessionUtils) {
        this.userRepo = userRepo;
        this.sessionUtils = sessionUtils;
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLoginForm(Model model, HttpServletRequest request) {
        Boolean isUserLoggedIn = false;
        if (sessionUtils.userExists(request)) {
            isUserLoggedIn = true;
        }
        model.addAttribute("user", new User());
        model.addAttribute("isUserLoggedIn", isUserLoggedIn);
        return "home";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String processLogin(User user, HttpServletRequest request) throws Exception {
        String loginUsername = user.getUsername();
        String passwordEncypted = encryptPassword(user.getPassword());
        User storedUser = getUser(loginUsername, passwordEncypted);
        if (storedUser != null) {
            sessionUtils.setUserSessionId(storedUser.getId(), request);
        }
        return "redirect:/login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String processLogout(HttpServletRequest request) {
        sessionUtils.clearSession(request);
        return "redirect:/login";
    }

    private String encryptPassword(String password) throws Exception {
        byte[] bytes = password.getBytes("UTF-8");
        return DigestUtils.md5DigestAsHex(bytes);
    }

    private User getUser(String loginUsername, String passwordEncypted) {
        return userRepo.findUserByUsernameAndPassword(loginUsername, passwordEncypted);
    }

}
