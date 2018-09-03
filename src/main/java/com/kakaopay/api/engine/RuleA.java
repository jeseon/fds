package com.kakaopay.api.engine;

import com.kakaopay.api.domain.AccountLog;
import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;

import java.time.LocalDateTime;
import java.util.List;

public class RuleA extends Rule {

    public RuleA(Fraud fraud, AccountLog accountLog, List<DepositLog> depositLogs, List<ReceivedLog> receivedLogs, List<RemittedLog> remittedLogs) {
        super(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
    }

    /**
     * 주어진 시간에 따른 잔액 구하기 (단, 마이너스 출금은 되지 않는 것으로 가정)
     *
     * @param dateTime 잔액을 구할 시간
     * @return long
     */
    public long getBalanceAtTime(LocalDateTime dateTime) {
        long depositAmount = 0;
        long receivedAmount = 0;
        long remittedAmount = 0;

        // 모든 충전 로그에서 user_id가 일치하고 주어진 시간 이전 로그일 경우 충전 금액 합산
        for (DepositLog depositLog: depositLogs) {
            if (depositLog.getUserId() == accountLog.getUserId() && depositLog.getCreatedAt().isBefore(dateTime)) {
                depositAmount += depositLog.getAmount();
            }
        }

        // 모든 받기 로그에서 user_id가 일치하고 주어진 시간 이전 로그일 경우 받기 금액 합산
        for (ReceivedLog receivedLog: receivedLogs) {
            if (receivedLog.getUserId() == accountLog.getUserId() && receivedLog.getCreatedAt().isBefore(dateTime)) {
                receivedAmount += receivedLog.getAmount();
            }
        }

        // 모든 송금 로그에서 user_id가 일치하고 주어진 시간 이전 로그일 경우 송금 금액 합산
        for (RemittedLog remittedLog: remittedLogs) {
            if (remittedLog.getUserId() == accountLog.getUserId() && remittedLog.getCreatedAt().isBefore(dateTime)) {
                remittedAmount += remittedLog.getAmount();
            }
        }

        // 충전 + 받기 - 송금 = 잔액
        return depositAmount + receivedAmount - remittedAmount;
    }

    /**
     * RuleA 유효성 확인
     *
     * 카카오머니 서비스 계좌 개설을 하고 1시간 이내, 20만원 충전 후 잔액이 1000원 이하가 되는 경우
     *
     * @return boolean
     */
    public boolean isValid() {
        LocalDateTime afterOneHour;

        for (DepositLog depositLog: depositLogs) {

            // 모든 충전 로그에서 user_id와 일치하는 로그만 조회
            if (depositLog.getUserId() == accountLog.getUserId()) {

                // 계좌 개설 1시간 이내 시간을 조회하기 위해 개설 1시간 추가
                afterOneHour = accountLog.getCreatedAt().plusHours(1);

                // 계좌 개설 1시간 이내 20만원 충전 조건 확인
                if (depositLog.getCreatedAt().isBefore(afterOneHour) && depositLog.getAmount() == 200000) {

                    // 개설 한시간 이후 잔액이 1000원 이하인 경우 확인
                    if (getBalanceAtTime(afterOneHour) <= 1000) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 유효성 검증 조건 문자열 전환
     *
     * @return String
     */
    @Override
    public String getInvalidRule() {
        return fraud.getInvalidRule() + (isValid() ? "" : ",RuleA");
    }
}
