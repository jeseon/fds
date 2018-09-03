package com.kakaopay.api.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceivedLog {
    LocalDateTime createdAt;
    long userId;
    String accountNo;
    long beforeBalance;
    String remittedAccountNo;
    long remittedUserId;
    long amount;
}
