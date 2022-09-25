package com.zink.bank.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Data
@Entity
@Table(name =  "user_account")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {

    @Id
    @Column(name = "account_no")
    private Integer accountNo;

    @Column(name = "cust_id")
    private Integer customerId;

    @Column(name= "customer_name")
    private String customerName;

    @Column(name = "status")
    private String status;

    @Column(name = "clear_balance")
    private double clearBalance;

    @Column(name = "unclear_balance")
    private double unclearBalance;

    @Column(name = "last_transaction_date")
    private Date lastTransactionDate;

}