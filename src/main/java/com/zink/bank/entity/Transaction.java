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
@Table(name = "transaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "account_no")
    private Integer accountNo;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "current_balance")
    private double currentBalance;

    @Column(name = "transaction_amount")
    private double transactionAmount;

    @Column(name = "clear_balance")
    private double clearBalance;

    @Column(name = "status")
    private String status;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "remarks")
    private String remarks;

}