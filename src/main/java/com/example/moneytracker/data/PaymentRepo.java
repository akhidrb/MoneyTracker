package com.example.moneytracker.data;

import com.example.moneytracker.models.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepo extends CrudRepository<Payment, Long> {

}
