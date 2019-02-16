package com.example.moneytracker.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "profit")
@SequenceGenerator(name = "profit_seq", sequenceName = "profit_id_seq", initialValue = 1)
public class Profit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profit_seq")
    private Long id;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "_date")
    private Date _date;

    @Column(name = "source")
    private String source;

    @Column(name = "amount")
    private Long amount;

    public Profit() {

    }

    public Profit(Long id, String source, Long amount) {
        this.id = id;
        this.source = source;
        this.amount = amount;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
