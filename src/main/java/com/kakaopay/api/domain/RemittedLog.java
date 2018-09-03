package com.kakaopay.api.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RemittedLog {
    LocalDateTime createdAt;
    long userId;
    String accountNo;
    long beforeBalance;
    String receivedAccountNo;
    long receivedUserId;
    long amount;
}
