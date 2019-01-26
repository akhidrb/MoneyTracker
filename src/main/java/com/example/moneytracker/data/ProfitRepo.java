package com.example.moneytracker.data;

import com.example.moneytracker.models.Profit;
import org.springframework.data.repository.CrudRepository;

public interface ProfitRepo extends CrudRepository<Profit, Long> {

}
