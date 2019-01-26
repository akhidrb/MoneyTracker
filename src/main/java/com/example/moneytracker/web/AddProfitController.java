package com.example.moneytracker.web;

import com.example.moneytracker.data.ProfitRepo;
import com.example.moneytracker.models.Profit;
import com.example.moneytracker.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/profit")
public class AddProfitController {

    private final ProfitRepo profitRepo;
    private final SessionUtils sessionUtils;

    @Autowired
    public AddProfitController(ProfitRepo profitRepo, SessionUtils sessionUtils) {
        this.profitRepo = profitRepo;
        this.sessionUtils = sessionUtils;
    }

    @GetMapping
    public String showAddForm(Model model, HttpServletRequest request) {
        if (!sessionUtils.userExists(request)) {
            return "redirect:/login";
        }
        model.addAttribute("profit", new Profit());
        return "profit-form";
    }

    @PostMapping
    public String processPayment(Profit profit, HttpServletRequest request) {
        if (!sessionUtils.userExists(request)) {
            return "redirect:/login";
        }
        addUserIdToProfit(profit, request);
        addCurrentDateToPayment(profit);
        profitRepo.save(profit);
        return "redirect:/payment";
    }

    private void addUserIdToProfit(Profit profit, HttpServletRequest request) {
        Long userId = sessionUtils.getUserSessionId(request);
        profit.setUserId(userId);
    }

    private void addCurrentDateToPayment(Profit profit) {
        profit.setDate(new Date());
    }

}
