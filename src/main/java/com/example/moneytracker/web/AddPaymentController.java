package com.example.moneytracker.web;

import com.example.moneytracker.data.PaymentRepo;
import com.example.moneytracker.models.Payment;
import com.example.moneytracker.utils.AuthenticationUtils;
import com.example.moneytracker.utils.SessionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
    private final AuthenticationUtils authenticationUtils;

    @Autowired
    public AddPaymentController(PaymentRepo paymentRepo,
                                SessionUtils sessionUtils,
                                AuthenticationUtils authenticationUtils) {
        this.paymentRepo = paymentRepo;
        this.sessionUtils = sessionUtils;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping
    public String showAddForm(Model model, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return "redirect:/home";
        }


        model.addAttribute("payment", new Payment());
        return "payment-form";
    }

    @PostMapping
    public String processPayment(Payment payment, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return "redirect:/home";
        }
        if (!authenticationUtils.paymentDetailsAuthentication(payment)) {
            return "redirect:/payment/error";
        }

        addUserIdToPayment(payment, request);
        addCurrentDateToPayment(payment);
        paymentRepo.save(payment);
        return "redirect:/payment";
    }

    @GetMapping("/error")
    public String processPaymentError(Model model, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return "redirect:/home";
        }
        model.addAttribute("payment", new Payment());
        model.addAttribute("error", new Error("Payment Entry Error!"));
        return "payment-form";
    }

    private void addUserIdToPayment(Payment payment, HttpServletRequest request) {
        Long userId = sessionUtils.getUserSessionId(request);
        payment.setUserId(userId);
    }

    private void addCurrentDateToPayment(Payment payment) {
        payment.setDate(new Date());
    }

}
