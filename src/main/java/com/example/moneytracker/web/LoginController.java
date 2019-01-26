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
        if (sessionUtils.userExists(request)) {
            return "redirect:/payment";
        }
        model.addAttribute("user", new User());
        return "home";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String processLogin(User user, HttpServletRequest request) throws Exception {
        String loginUsername = user.getUsername();
        String passwordEncypted = encryptPassword(user);
        User storedUser = getUser(loginUsername, passwordEncypted);
        if (storedUser == null) {
            return "redirect:/login";
        }
        sessionUtils.setUserSessionId(storedUser.getId(), request);
        return "redirect:/payment";


    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String processLogout(HttpServletRequest request) {
        sessionUtils.clearSession(request);
        return "redirect:/login";
    }

    private String encryptPassword(User user) throws Exception {
        String loginPassword = user.getPassword();
        byte[] password = loginPassword.getBytes("UTF-8");
        return DigestUtils.md5DigestAsHex(password);
    }

    private User getUser(String loginUsername, String passwordEncypted) {
        return userRepo.findUserByUsernameAndPassword(loginUsername, passwordEncypted);
    }

}
