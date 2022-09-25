package com.zink.bank.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "denominations")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Denominations {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Integer value;

    @Column(name = "count")
    private Integer count;

    @Column(name = "deposit_limit")
    private Integer depositLimit;

    @Column(name = "disburse_limit")
    private Integer disburseLimit;

    @Column(name = "active")
    private boolean active;

}