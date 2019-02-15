package com.example.moneytracker.data;

import com.example.moneytracker.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

    User findUserById(Long userId);

    User findUserByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);

}
