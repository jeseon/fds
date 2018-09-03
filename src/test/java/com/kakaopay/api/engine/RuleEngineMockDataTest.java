package com.kakaopay.api.engine;

import com.kakaopay.api.domain.AccountLog;
import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RuleEngineMockDataTest {
    private AccountLog accountLog;
    private List<DepositLog> depositLogs;
    private List<ReceivedLog> receivedLogs;
    private List<RemittedLog> remittedLogs;

    @Before
    public void before() {
        depositLogs = new ArrayList<>();
        receivedLogs = new ArrayList<>();
        remittedLogs = new ArrayList<>();

        long userId = 1000;
        long receivedUserId = 2000;
        long remittedUserIdRuleB = 3333;
        long remittedUserIdRuleC = 4444;
        long receivedAmountRuleB = 100000;
        long receivedAmountRuleC = 50000;
        String accountNo = "1111-11111-1111-1";
        String receivedAccountNo = "2222-22222-2222-2";
        String remittedAccountNo = "3333-33333-3333-3";
        LocalDateTime accountCreatedAt = LocalDateTime.of(2018, 9, 1, 1, 0, 0);
        LocalDateTime depositCreatedAt = LocalDateTime.of(2018, 9, 1, 1, 1, 0);
        LocalDateTime remittedCreatedAt = LocalDateTime.of(2018, 9, 1, 1, 2, 0);
        LocalDateTime receivedCreatedAt = LocalDateTime.of(2018, 9, 2, 1, 0, 0);
        LocalDateTime currentDateTime = LocalDateTime.of(2018, 9, 1, 3, 0, 0);

        accountLog = new AccountLog();
        accountLog.setCreatedAt(accountCreatedAt);
        accountLog.setUserId(userId);
        accountLog.setAccountNo(accountNo);

        DepositLog depositLog = new DepositLog();
        depositLog.setCreatedAt(depositCreatedAt);
        depositLog.setUserId(userId);
        depositLog.setAccountNo(accountNo);
        depositLog.setAmount(200000);
        depositLog.setBankAccountNo("bank-account-no-01");
        depositLogs.add(depositLog);

        RemittedLog remittedLog = new RemittedLog();
        remittedLog.setCreatedAt(remittedCreatedAt);
        remittedLog.setUserId(userId);
        remittedLog.setAccountNo(accountNo);
        remittedLog.setBeforeBalance(200000);
        remittedLog.setReceivedAccountNo(receivedAccountNo);
        remittedLog.setReceivedUserId(receivedUserId);
        remittedLog.setAmount(199500);
        remittedLogs.add(remittedLog);

        for (int i = 0; i < 5; i++) {
            ReceivedLog receivedLog = new ReceivedLog();
            long beforeBalance = (receivedLog.getBeforeBalance() + receivedAmountRuleB) * i;

            receivedLog.setCreatedAt(receivedCreatedAt.plusDays(i));
            receivedLog.setUserId(userId);
            receivedLog.setAccountNo(accountNo);
            receivedLog.setBeforeBalance(beforeBalance);
            receivedLog.setRemittedAccountNo(remittedAccountNo);
            receivedLog.setRemittedUserId(remittedUserIdRuleB);
            receivedLog.setAmount(receivedAmountRuleB);
            receivedLogs.add(receivedLog);
        }

        for (int i = 0; i < 3; i++) {
            ReceivedLog receivedLog = new ReceivedLog();
            long beforeBalance = (receivedLog.getBeforeBalance() + receivedAmountRuleC) * i;

            receivedLog.setCreatedAt(accountCreatedAt.plusHours(1).plusMinutes(10 * i));
            receivedLog.setUserId(userId);
            receivedLog.setAccountNo(accountNo);
            receivedLog.setBeforeBalance(beforeBalance);
            receivedLog.setRemittedAccountNo(remittedAccountNo);
            receivedLog.setRemittedUserId(remittedUserIdRuleC);
            receivedLog.setAmount(receivedAmountRuleC);
            receivedLogs.add(receivedLog);
        }
    }

    @Test
    public void getInvalidRules() {
        Fraud fraud = new FraudRule();
        fraud = new RuleA(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
        fraud = new RuleB(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
        fraud = new RuleC(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);

        assertEquals("RuleA,RuleB", fraud.getInvalidRule().replaceFirst(",", ""));
    }
}