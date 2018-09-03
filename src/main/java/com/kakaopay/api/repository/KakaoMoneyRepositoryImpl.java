package com.kakaopay.api.repository;

import com.kakaopay.api.domain.DepositLog;
import com.kakaopay.api.domain.ReceivedLog;
import com.kakaopay.api.domain.RemittedLog;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class KakaoMoneyRepositoryImpl implements KakaoMoneyRepository {
    public List<DepositLog> getDepositLogs() {
        CSVReader reader;
        String[] nextLine;
        ArrayList<DepositLog> depositLogs = new ArrayList<>();

        try {
            ClassPathResource classPathResource = new ClassPathResource("data/deposit_log.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            reader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

            while ((nextLine = reader.readNext()) != null) {
                DepositLog depositLog = new DepositLog();
                depositLog.setCreatedAt(LocalDateTime.parse(nextLine[0]));
                depositLog.setUserId(Long.parseLong(nextLine[1]));
                depositLog.setAccountNo(nextLine[2]);
                depositLog.setAmount(Long.parseLong(nextLine[3]));
                depositLog.setBankAccountNo(nextLine[4]);
                depositLogs.add(depositLog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return depositLogs;
    }

    public List<ReceivedLog> getReceivedLogs() {
        CSVReader reader;
        String[] nextLine;
        ArrayList<ReceivedLog> receivedLogs = new ArrayList<>();

        try {
            ClassPathResource classPathResource = new ClassPathResource("data/received_log.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            reader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

            while ((nextLine = reader.readNext()) != null) {
                ReceivedLog receivedLog = new ReceivedLog();
                receivedLog.setCreatedAt(LocalDateTime.parse(nextLine[0]));
                receivedLog.setUserId(Long.parseLong(nextLine[1]));
                receivedLog.setAccountNo(nextLine[2]);
                receivedLog.setBeforeBalance(Long.parseLong(nextLine[3]));
                receivedLog.setRemittedAccountNo(nextLine[4]);
                receivedLog.setRemittedUserId(Long.parseLong(nextLine[5]));
                receivedLog.setAmount(Long.parseLong(nextLine[6]));
                receivedLogs.add(receivedLog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receivedLogs;
    }

    public List<RemittedLog> getRemittedLogs() {
        CSVReader reader;
        String[] nextLine;
        ArrayList<RemittedLog> remittedLogs = new ArrayList<>();

        try {
            ClassPathResource classPathResource = new ClassPathResource("data/remitted_log.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            reader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

            while ((nextLine = reader.readNext()) != null) {
                RemittedLog remittedLog = new RemittedLog();
                remittedLog.setCreatedAt(LocalDateTime.parse(nextLine[0]));
                remittedLog.setUserId(Long.parseLong(nextLine[1]));
                remittedLog.setAccountNo(nextLine[2]);
                remittedLog.setBeforeBalance(Long.parseLong(nextLine[3]));
                remittedLog.setReceivedAccountNo(nextLine[4]);
                remittedLog.setReceivedUserId(Long.parseLong(nextLine[5]));
                remittedLog.setAmount(Long.parseLong(nextLine[6]));
                remittedLogs.add(remittedLog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return remittedLogs;
    }
}
