package com.example.moneytracker.utils;

import com.example.moneytracker.models.Payment;
import com.example.moneytracker.models.Profit;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {

    public Boolean profitDetailsAuthentication(Profit profit) {
        if (!entryExists(profit.getSource())
                || !entryExists(profit.getAmount())) {
            return false;
        }
        return true;
    }

    public Boolean paymentDetailsAuthentication(Payment payment) {
        if (!entryExists(payment.getItem())
                || !entryExists(payment.getAmountPaid())) {
            return false;
        }
        return true;
    }


    private Boolean entryExists(Object entry) {
        if (entry != null) {
            return true;
        }
        return false;
    }

}
