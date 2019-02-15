package com.example.moneytracker.web;

import com.example.moneytracker.data.UserRepo;
import com.example.moneytracker.models.User;
import com.example.moneytracker.utils.SessionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    private final UserRepo userRepo;
    private final SessionUtils sessionUtils;

    @Autowired
    public HomeController(UserRepo userRepo, SessionUtils sessionUtils) {
        this.userRepo = userRepo;
        this.sessionUtils = sessionUtils;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showLoginForm(Model model, HttpServletRequest request) {
        Boolean isUserLoggedIn = false;
        if (sessionUtils.userExists(request)) {
            isUserLoggedIn = true;
            Long userId = sessionUtils.getUserSessionId(request);
            model.addAttribute("user", userRepo.findUserById(userId));
        } else {
            model.addAttribute("user", new User());
        }
        model.addAttribute("isUserLoggedIn", isUserLoggedIn);
        ///////////////////
        String name = "World";

        Subject subject = SecurityUtils.getSubject();

        PrincipalCollection principalCollection = subject.getPrincipals();

        if (principalCollection != null && !principalCollection.isEmpty()) {
            Collection<Map> principalMaps = subject.getPrincipals().byType(Map.class);
            if (CollectionUtils.isEmpty(principalMaps)) {
                name = subject.getPrincipal().toString();
            } else {
                name = (String) principalMaps.iterator().next().get("username");
            }
        }

        model.addAttribute("name", name);
        model.addAttribute("subject", subject);
        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String processLogin(User user, HttpServletRequest request) throws Exception {
        String loginUsername = user.getUsername();
        String passwordEncypted = encryptPassword(user.getPassword());
        User storedUser = getUserByUsernameAndPassword(loginUsername, passwordEncypted);
//        if (storedUser != null) {
//            sessionUtils.setUserSessionId(storedUser.getId(), request);
//        }
        return "redirect:/home";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String processLogout(HttpServletRequest request) {
        sessionUtils.clearSession(request);
        return "redirect:/home";
    }

    private String encryptPassword(String password) throws Exception {
        byte[] bytes = password.getBytes("UTF-8");
        return DigestUtils.md5DigestAsHex(bytes);
    }

    private User getUserByUsernameAndPassword(String loginUsername, String passwordEncypted) {
        return userRepo.findUserByUsernameAndPassword(loginUsername, passwordEncypted);
    }

}
