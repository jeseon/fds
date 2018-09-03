# FDS(Fraud Detection System)

 * Gradle 프로젝트로 SpringBoot를 활용한 Restful API 구현
 * 테스트 데이터의 경우 간단한 CSV로 작성

## Source 디렉토리 구조
```
/─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─kakaopay
    │  │          └─api
    │  │              ├─controller  # Restful Controller
    │  │              ├─domain      # Data Domain
    │  │              ├─engine      # FDS Engine 구현
    │  │              ├─repository  # Data Repository
    │  │              └─service     # FraudService (FDS Engine 실행)
    │  └─resources
    │     └─data                    # 테스트용 CSV 데이터 파일
    └─test
        └─java
           └─com
               └─kakaopay
                   └─api
                       └─engine     # FDS Engine 테스트
```

## FDS Engine

Rule 추가/삭제를 용의하게 하기 위해 Decorator 패턴 적용

### UML
![Engine UML](/doc/engine.png)

### 적용 방법
```java
/**
 * Rule 생성 후 FraudRule에 Rule을 계속 적용해 나갈 수 있다.
 */
Fraud fraud = new FraudRule();
fraud = new RuleA(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
fraud = new RuleB(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
fraud = new RuleC(fraud, accountLog, depositLogs, receivedLogs, remittedLogs);
```

## 다운로드

[FDS Application](https://github.com/jeseon/fds/releases/download/0.0.1/fds-0.0.1.war)


## 실행 방법
```
$ java -jar fds-0.0.1.war
```

## 테스트 방법
```
$ curl -i http://localhost:8080/v1/fraud/1111

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 03 Sep 2018 19:48:15 GMT

{"user_id":1111,"is_fraud":true,"rule":"RuleA"}
```
