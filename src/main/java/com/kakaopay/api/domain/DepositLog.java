package com.kakaopay.api.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepositLog {
    LocalDateTime createdAt;
    long userId;
    String accountNo;
    long amount;
    String bankAccountNo;
}
