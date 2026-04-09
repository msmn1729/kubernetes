# 05. Volume, PV, PVC, MySQL

## 범위

- Volume 개념
- MySQL Deployment 실행
- PV/PVC 기반 데이터 유지
- Spring Boot와 MySQL 연결

## 핵심 개념

- 컨테이너 내부 파일 시스템은 휘발적이다.
- 데이터베이스는 재시작 후에도 데이터가 남아야 한다.
- PV는 실제 저장소 자원, PVC는 그 자원을 요청하는 방식이다.
- MySQL은 외부 공개보다 내부 Service로 연결하는 편이 안전하다.

## 체크리스트

- [ ] MySQL Deployment 실행
- [ ] PV 생성
- [ ] PVC 생성 및 연결
- [ ] Spring Boot와 MySQL 연동
- [ ] DB 연결 실패 케이스 기록
- [ ] 외부 MySQL 접근 차단 개념 정리

## 시각 흐름

```text
+------------------+      +------+      +------+
| Spring Boot Pod  | ---> | PVC  | ---> | PV   |
+------------------+      +------+      +------+
          |
          v
+------------------+
| MySQL Service    |
+------------------+
```

## 실습 메모

- PVC 바인딩 결과:
- MySQL 접속 확인:
- Spring Boot 연동 확인:
- 연결 실패 시 로그:

