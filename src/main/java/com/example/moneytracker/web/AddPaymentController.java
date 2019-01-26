package com.example.moneytracker.web;

import com.example.moneytracker.data.PaymentRepo;
import com.example.moneytracker.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/payment")
public class AddPaymentController {

    private final PaymentRepo paymentRepo;

    @Autowired
    public AddPaymentController(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @GetMapping
    public String showAddForm(Model model, HttpServletRequest request) {

        if (!correctUserDetails(request)) {
            return "redirect:/login";
        }

        model.addAttribute("payment", new Payment());
        return "payment-form";
    }

    @PostMapping
    public String processPayment(Payment payment, HttpServletRequest request) {
        if (!correctUserDetails(request)) {
            return "redirect:/login";
        }
        addCurrentDateToPayment(payment);
        paymentRepo.save(payment);
        return "redirect:/payment";
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

    private void addCurrentDateToPayment(Payment payment) {
        payment.setDate(new Date());
    }

}
