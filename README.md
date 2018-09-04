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
                           ├─RuleEngineMockDataTest  # Mockup Data 테스트 (RuleA/B/C 모두 위반 여부 확인)
                           └─RuleEngineTest          # CSV Data 테스트 (RuleA/B/C 각각 위반 여부 확인)
```

## FDS Engine

Rule 추가/삭제를 용의하게 하기 위해 Decorator 패턴 적용

### UML
![Engine UML](/doc/engine.png)

### 클래스 설명
 * Fraud: 공통 인터페이스
 * FraudRule: Fraud에 Rule을 적용할 클래스 
 * Rule: 데코레이터 추상 클래스
 * RuleA: FraudRule 객체를 감싸는 데코레이터 RuleA
 * RuleB: FraudRule 객체를 감싸는 데코레이터 RuleB
 * RuleC: FraudRule 객체를 감싸는 데코레이터 RuleC

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

[FDS Application](https://github.com/jeseon/fds/releases/download/0.0.1/fds-v0.0.1-jeseon.war)


## 실행 방법
```
$ java -jar fds-v0.0.1-jeseon.war
```

## 테스트 예시
### Invalid user_id 1111 (violated RuleA)
```
$ curl -i http://localhost:8080/v1/fraud/1111
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 03 Sep 2018 21:05:45 GMT

{"user_id":1111,"is_fraud":true,"rule":"RuleA"}
```

### Invalid user_id 8888 (violated RuleB)
```
$ curl -i http://localhost:8080/v1/fraud/8888
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 03 Sep 2018 21:06:03 GMT

{"user_id":8888,"is_fraud":true,"rule":"RuleB"}
```

### Valid user_id 2000 (passed RuleA, RuleB and RuleC)
```
$ curl -i http://localhost:8080/v1/fraud/2000
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 03 Sep 2018 21:07:50 GMT

{"user_id":2000,"is_fraud":false,"rule":""}
```

## Epilogue
 * 테스트 데이터를 DB로 할걸 그랬나 계속 생각함 (CSV로 만들기 생각보다 힘들)
 * 계좌잔액조회는 생각해볼 여지가 많이 남아있음... 

### 풀이 순서
 * 먼저 Rule에 따른 제약조건 구현 후 유연(?)하게 적용가능한 방법 결정
 * RuleC -> RuleB -> RuleA 순으로 작성 (RuleA에서 잔액을 요구해서 조금 고민함)

### 참고: 계좌잔액 제약조건
 1. 송금 시스템은 잔액이상을 송금 불가 (단일 책임 원칙)
 1. 구현된 잔액조회는 로그만으로 판단
 1. 제약조건은 외부에서 수정 불가
