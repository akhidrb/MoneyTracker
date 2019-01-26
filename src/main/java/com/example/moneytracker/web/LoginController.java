package com.example.moneytracker.web;

import com.example.moneytracker.data.UserRepo;
import com.example.moneytracker.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {

    private final UserRepo userRepo;

    @Autowired
    public LoginController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLoginForm(Model model, HttpServletRequest request) {
        if (correctUserDetails(request)) {
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
        setSessionIfUserExists(storedUser, request);
        return redirectByCheckingUserInfo(storedUser);
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String processLogout(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("currentUser");
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

    private String redirectByCheckingUserInfo(User storedUser) {
        if (storedUser != null) {
            return "redirect:/payment";
        }
        return "redirect:/login";
    }

    private void setSessionIfUserExists(User storedUser, HttpServletRequest request) {
        if (request != null && storedUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", storedUser.getId());
        }
    }

    private Boolean correctUserDetails(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object currentUserId = session.getAttribute("currentUser");
        if (currentUserId == null) {
            return false;
        }
        if (Long.parseLong(currentUserId.toString()) != 1) {
            return false;
        }
        return true;
    }

}
