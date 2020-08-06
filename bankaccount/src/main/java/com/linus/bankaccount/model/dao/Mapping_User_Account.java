package com.linus.bankaccount.model.dao;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Mapping_User_Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uaid;

    private int userid;
    private int atid;

    @Column(unique = true)
    private String account_number;

    private double balance_amount;

    @CreatedDate
    private Date created_date;

    @LastModifiedDate
    private Date last_modified;


    public int getUaid() {
        return uaid;
    }

    public void setUaid(int uaid) {
        this.uaid = uaid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getAtid() {
        return atid;
    }

    public void setAtid(int atid) {
        this.atid = atid;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public double getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(double balance_amount) {
        this.balance_amount = balance_amount;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(Date last_modified) {
        this.last_modified = last_modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mapping_User_Account that = (Mapping_User_Account) o;
        return uaid == that.uaid &&
                userid == that.userid &&
                atid == that.atid &&
                Double.compare(that.balance_amount, balance_amount) == 0 &&
                account_number.equals(that.account_number) &&
                created_date.equals(that.created_date) &&
                last_modified.equals(that.last_modified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uaid, userid, atid, account_number, balance_amount, created_date, last_modified);
    }
}
