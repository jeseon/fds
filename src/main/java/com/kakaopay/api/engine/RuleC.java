package com.kakaopay.api.engine;

import com.kakaopay.api.domain.AccountLog;
import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RuleC extends Rule {

    public RuleC(Fraud fraud, AccountLog accountLog, List<DepositLog> depositLogs, List<ReceivedLog> receivedLogs, List<RemittedLog> remittedLogs) {
        super(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
    }

    /**
     * RuleC 유효성 확인
     *
     * 2시간 이내, 카카오머니 받기로 5만원 이상 금액을 3회 이상 하는 경우
     *
     * @return boolean
     */
    public boolean isValid() {
        long diffHours;
        int falseCount = 0;

        for (ReceivedLog receivedLog: receivedLogs) {

            // 모든 받기 로그에서 user_id와 일치하는 로그만 조회
            if (receivedLog.getUserId() == accountLog.getUserId()) {

                // 받기 로그와 현재 시간 차이 계산
                diffHours = ChronoUnit.HOURS.between(receivedLog.getCreatedAt(), LocalDateTime.now());

                // 2시간 이내 5만원 이상 받기 회수 누적
                if (diffHours <= 2 && receivedLog.getAmount() >= 50000) {
                    falseCount++;
                }
            }
        }

        // 3회 미만일 경우만 유효
        return falseCount < 3;
    }

    /**
     * 유효성 검증 조건 문자열 전환
     *
     * @return String
     */
    @Override
    public String getInvalidRule() {
        return fraud.getInvalidRule() + (isValid() ? "" : ",RuleC");
    }
}
