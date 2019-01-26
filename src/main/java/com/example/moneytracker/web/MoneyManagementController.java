package com.example.moneytracker.web;


import com.example.moneytracker.data.PaymentRepo;
import com.example.moneytracker.data.ProfitRepo;
import com.example.moneytracker.models.Payment;
import com.example.moneytracker.models.Profit;
import com.example.moneytracker.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/manage")
public class MoneyManagementController {

    private final PaymentRepo paymentRepo;
    private final ProfitRepo profitRepo;
    private final SessionUtils sessionUtils;

    @Autowired
    public MoneyManagementController(PaymentRepo paymentRepo,
                                     ProfitRepo profitRepo,
                                     SessionUtils sessionUtils) {
        this.paymentRepo = paymentRepo;
        this.profitRepo = profitRepo;
        this.sessionUtils = sessionUtils;
    }

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public String showLoginForm(Model model, HttpServletRequest request) {
        if (!sessionUtils.userExists(request)) {
            return "redirect:/login";
        }
        List<Payment> paymentList = findPaymentsForThisMonth(request);
        model.addAttribute("payments", paymentList);
        model.addAttribute("totalPaymentsForCurrentMonth",
                totalPaymentsForCurrentMonth(paymentList));
        model.addAttribute("balanceOfCurrentMonth", findBalance(request));
        return "manage-form";
    }

    private long totalPaymentsForCurrentMonth(List<Payment> paymentList) {
        long totalPayments = 0;
        for (Payment payment : paymentList) {
            totalPayments += payment.getAmountPaid();
        }
        return totalPayments;
    }

    private List<Payment> findPaymentsForThisMonth(HttpServletRequest request) {
        Long userId = sessionUtils.getUserSessionId(request);
        Iterable<Payment> payments = paymentRepo.findAllByUserId(userId);
        int currentMonth = getCurrentMonth();
        List<Payment> paymentList = new ArrayList<Payment>();
        addPaymentsOfCurrentMonthToList(paymentList, payments, currentMonth);
        return paymentList;
    }

    private Long findBalance(HttpServletRequest request) {
        Long userId = sessionUtils.getUserSessionId(request);
        Iterable<Payment> payments = paymentRepo.findAllByUserId(userId);
        Iterable<Profit> profits = profitRepo.findAllByUserId(userId);
        int currentMonth = getCurrentMonth();
        List<Payment> paymentList = new ArrayList<Payment>();
        addPaymentsOfCurrentMonthToList(paymentList, payments, currentMonth);
        List<Profit> profitList = new ArrayList<Profit>();
        addProfitsOfCurrentMonthToList(profitList, profits, currentMonth);
        return balanceFromProfitsAndPayments(profitList, paymentList);
    }

    private int getCurrentMonth() {
        Date currentDate = new Date();
        return currentDate.getMonth();
    }

    private void addPaymentsOfCurrentMonthToList(List<Payment> paymentList,
                                                 Iterable<Payment> payments,
                                                 int currentMonth) {
        payments.forEach(payment -> {
            Date paymentDate = payment.getDate();
            if (paymentDate.getMonth() == currentMonth) {
                paymentList.add(payment);
            }
        });
    }

    private void addProfitsOfCurrentMonthToList(List<Profit> profitList,
                                                Iterable<Profit> profits,
                                                int currentMonth) {
        profits.forEach(profit -> {
            Date profitDate = profit.getDate();
            if (profitDate.getMonth() == currentMonth) {
                profitList.add(profit);
            }
        });
    }


    private long balanceFromProfitsAndPayments(List<Profit> profitList,
                                               List<Payment> paymentList) {
        long totalProfitForCurrentMonth = 0;
        for (Profit profit : profitList) {
            totalProfitForCurrentMonth += profit.getAmount();
        }
        long totalPaymentsForCurrentMonth = 0;
        for (Payment payment : paymentList) {
            totalPaymentsForCurrentMonth += payment.getAmountPaid();
        }
        return (totalProfitForCurrentMonth - totalPaymentsForCurrentMonth);
    }

}
