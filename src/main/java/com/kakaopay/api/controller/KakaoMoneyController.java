package com.kakaopay.api.controller;

import com.kakaopay.api.domain.Account;
import com.kakaopay.api.domain.AccountLog;
import com.kakaopay.api.service.KakaoMoneyService;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class KakaoMoneyController implements ErrorController {
    private final KakaoMoneyService kakaoMoneyService;

    @GetMapping("/v1/fraud/{userId:[\\d]+}")
    public Account detectFraudByUserId(@PathVariable long userId) {
        return kakaoMoneyService.getAccountWithInvalidRules(userId);
    }

    @GetMapping("/error")
    public Map<String, String> handleError() {
        Map<String, String> error = new HashMap<>();
        error.put("message", "not found");

        return error;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
