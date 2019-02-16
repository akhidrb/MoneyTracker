package com.example.moneytracker.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
@SequenceGenerator(name = "payment_seq", sequenceName = "payment_id_seq", initialValue = 1)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    private Long id;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "amountpaid")
    private Long amountPaid;

    @Column(name = "_date")
    private Date _date;

    @Column(name = "item")
    private String item;

    public Payment() {
    }

    public Payment(Long id, Long amountPaid, String item) {
        this.id = id;
        this.amountPaid = amountPaid;
        this.item = item;
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

    public Long getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Long amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
