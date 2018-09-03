package com.kakaopay.api.domain;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.time.LocalDateTime;

@Data
public class AccountLog {
    LocalDateTime createdAt;
    long userId;
    String accountNo;

    public AccountLog() {
    }

    public AccountLog(long userId) {
        CSVReader reader;
        String[] nextLine;

        try {
            ClassPathResource classPathResource = new ClassPathResource("data/account_log.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            reader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

            while ((nextLine = reader.readNext()) != null) {
                if (userId == Long.parseLong(nextLine[1])) {
                    this.setCreatedAt(LocalDateTime.parse(nextLine[0]));
                    this.setUserId(Long.parseLong(nextLine[1]));
                    this.setAccountNo(nextLine[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
