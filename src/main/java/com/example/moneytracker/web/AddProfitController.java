package com.example.moneytracker.web;

import com.example.moneytracker.data.ProfitRepo;
import com.example.moneytracker.models.Profit;
import com.example.moneytracker.utils.AuthenticationUtils;
import com.example.moneytracker.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/profit")
public class AddProfitController {

    private final ProfitRepo profitRepo;
    private final SessionUtils sessionUtils;
    private final AuthenticationUtils authenticationUtils;

    private static final String PROFIT = "profit";


    @Autowired
    public AddProfitController(ProfitRepo profitRepo,
                               SessionUtils sessionUtils,
                               AuthenticationUtils authenticationUtils) {
        this.profitRepo = profitRepo;
        this.sessionUtils = sessionUtils;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping
    public String showAddForm(HttpServletRequest request) {
        return serializeResponse("Hello");
    }

    @PostMapping
    public String processProfit(Profit profit, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return "redirect:/home";
        }
        if (!authenticationUtils.profitDetailsAuthentication(profit)) {
            return "redirect:/profit/error";
        }

        addUserIdToProfit(profit, request);
        addCurrentDateToPayment(profit);
        profitRepo.save(profit);
        return "redirect:/profit";
    }

    @GetMapping("/error")
    public String processProfitError(Model model, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return "redirect:/home";
        }
        model.addAttribute("profit", new Profit());
        model.addAttribute("error", new Error("Profit Entry Error!"));
        return "profit-form";
    }


    private void addUserIdToProfit(Profit profit, HttpServletRequest request) {
        Long userId = sessionUtils.getUserSessionId(request);
        profit.setUserId(userId);
    }

    private void addCurrentDateToPayment(Profit profit) {
        profit.set_date(new Date());
    }


    public String serializeResponse(String response) {
        JsonObject successResponse = new JsonObject();
        Gson gson = new Gson();
        successResponse.add(PROFIT,
                gson.toJsonTree(response));
        return successResponse.toString();
    }

    private Object deserializeRequestBody(String valueObject, Class<?> valueObjectClass) {
        Gson gson = new Gson();
        Object serializedValueObject = gson.fromJson(valueObject, valueObjectClass);
        return serializedValueObject;
    }

}
