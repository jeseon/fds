package com.kakaopay.api.domain;

import lombok.Data;

@Data
public class Account {
    long userId;
    Boolean isFraud;
    String rule;
}
