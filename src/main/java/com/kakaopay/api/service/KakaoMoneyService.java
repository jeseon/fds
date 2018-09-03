package com.kakaopay.api.service;

import com.kakaopay.api.domain.*;
import com.kakaopay.api.engine.*;
import com.kakaopay.api.repository.KakaoMoneyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KakaoMoneyService {
    private final KakaoMoneyRepository kakaoMoneyRepository;

    public Account getAccountWithInvalidRules(long userId) {
        AccountLog accountLog = new AccountLog(userId);
        List<DepositLog> depositLogs = kakaoMoneyRepository.getDepositLogs();
        List<ReceivedLog> receivedLogs = kakaoMoneyRepository.getReceivedLogs();
        List<RemittedLog> remittedLogs = kakaoMoneyRepository.getRemittedLogs();

        // Fraud 탐지 Rule 적용
        // 데코레이터 패턴 적용으로 Rule을 쉽게 추가/삭제 할 수 있다.
        Fraud fraud = new FraudRule();
        fraud = new RuleA(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
        fraud = new RuleB(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
        fraud = new RuleC(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);

        String invalidRules = fraud.getInvalidRule().replaceFirst(",", "");

        Account account = new Account();
        account.setUserId(accountLog.getUserId());
        account.setIsFraud(!invalidRules.isEmpty());
        account.setRule(invalidRules);

        return account;
    }
}
