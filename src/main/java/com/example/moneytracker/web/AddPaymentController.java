package com.example.moneytracker.web;

import com.example.moneytracker.data.PaymentRepo;
import com.example.moneytracker.models.Payment;
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
@RequestMapping("/payment")
public class AddPaymentController {

    private final PaymentRepo paymentRepo;
    private final SessionUtils sessionUtils;

    @Autowired
    public AddPaymentController(PaymentRepo paymentRepo, SessionUtils sessionUtils) {
        this.paymentRepo = paymentRepo;
        this.sessionUtils = sessionUtils;
    }

    @GetMapping
    public String showAddForm(Model model, HttpServletRequest request) {
        if (!sessionUtils.userExists(request)) {
            return "redirect:/login";
        }
        model.addAttribute("payment", new Payment());
        return "payment-form";
    }

    @PostMapping
    public String processPayment(Payment payment, HttpServletRequest request) {
        if (!sessionUtils.userExists(request)) {
            return "redirect:/login";
        }
        addUserIdToPayment(payment, request);
        addCurrentDateToPayment(payment);
        paymentRepo.save(payment);
        return "redirect:/payment";
    }

    private void addUserIdToPayment(Payment payment, HttpServletRequest request) {
        Long userId = sessionUtils.getUserSessionId(request);
        payment.setUserId(userId);
    }

    private void addCurrentDateToPayment(Payment payment) {
        payment.setDate(new Date());
    }

}
