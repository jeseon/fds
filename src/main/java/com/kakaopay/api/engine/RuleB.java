package com.kakaopay.api.engine;

import com.kakaopay.api.domain.AccountLog;
import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class RuleB extends Rule {

    public RuleB(Fraud fraud, AccountLog accountLog, List<DepositLog> depositLogs, List<ReceivedLog> receivedLogs, List<RemittedLog> remittedLogs) {
        super(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
    }

    /**
     * RuleB 유효성 확인
     *
     * 카카오머니 서비스 계좌 개설을 하고 7일 이내, 카카오머니 받기로 10만원 이상 금액을 5회 이상 하는 경우
     *
     * @return boolean
     */
    public boolean isValid() {
        long diffDays;
        int falseCount = 0;

        for (ReceivedLog receivedLog: receivedLogs) {

            // 모든 받기 로그에서 user_id와 일치하는 로그만 조회
            if (receivedLog.getUserId() == accountLog.getUserId()) {

                // 받기 로그와 계좌 개설 간 날짜 차이 계산
                diffDays = ChronoUnit.DAYS.between(receivedLog.getCreatedAt(), accountLog.getCreatedAt());

                // 계좌 개설 후 7일 이내 10만원 이상 받기 회수 누적
                if (diffDays <= 7 && receivedLog.getAmount() >= 100000) {
                    falseCount++;
                }
            }
        }

        // 5회 미만일 경우만 유효
        return falseCount < 5;
    }

    /**
     * 유효성 검증 조건 문자열 전환
     *
     * @return String
     */
    @Override
    public String getInvalidRule() {
        return fraud.getInvalidRule() + (isValid() ? "" : ",RuleB");
    }
}
