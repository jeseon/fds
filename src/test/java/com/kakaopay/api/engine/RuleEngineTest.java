package com.kakaopay.api.engine;

import com.kakaopay.api.domain.AccountLog;
import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;
import com.kakaopay.api.repository.KakaoMoneyRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RuleEngineTest {
    private List<DepositLog> depositLogs;
    private List<ReceivedLog> receivedLogs;
    private List<RemittedLog> remittedLogs;

    @Autowired
    private KakaoMoneyRepositoryImpl kakaoMoneyRepository;

    @Before
    public void before() {
        depositLogs = kakaoMoneyRepository.getDepositLogs();
        receivedLogs = kakaoMoneyRepository.getReceivedLogs();
        remittedLogs = kakaoMoneyRepository.getRemittedLogs();
    }

    @Test
    public void isValidRuleA() {
        AccountLog accountLog = new AccountLog(1111);
        Fraud fraud = new FraudRule();
        fraud = new RuleA(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);

        assertEquals("RuleA", fraud.getInvalidRule().replaceFirst(",", ""));
    }

    @Test
    public void isValidRuleB() {
        AccountLog accountLog = new AccountLog(8888);

        Fraud fraud = new FraudRule();
        fraud = new RuleB(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);

        assertEquals("RuleB", fraud.getInvalidRule().replaceFirst(",", ""));
    }

    @Test
    public void isValidRuleC() {
        AccountLog accountLog = new AccountLog(9999);
        Fraud fraud = new FraudRule();
        fraud = new RuleC(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);

        assertEquals("", fraud.getInvalidRule().replaceFirst(",", ""));
    }
}