package com.kakaopay.api.repository;

import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KakaoMoneyRepository {
    List<DepositLog> getDepositLogs();
    List<ReceivedLog> getReceivedLogs();
    List<RemittedLog> getRemittedLogs();
}
