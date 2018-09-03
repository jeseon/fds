package com.kakaopay.api.engine;

import com.kakaopay.api.domain.AccountLog;
import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;

import java.util.List;

public abstract class Rule implements Fraud {
    protected Fraud fraud;
    protected AccountLog accountLog;
    protected List<DepositLog> depositLogs;
    protected List<ReceivedLog> receivedLogs;
    protected List<RemittedLog> remittedLogs;

    public Rule(Fraud fraud, AccountLog accountLog, List<DepositLog> depositLogs, List<ReceivedLog> receivedLogs, List<RemittedLog> remittedLogs) {
        this.fraud = fraud;
        this.accountLog = accountLog;
        this.depositLogs = depositLogs;
        this.receivedLogs = receivedLogs;
        this.remittedLogs = remittedLogs;
    }

    @Override
    public abstract String getInvalidRule();
}
