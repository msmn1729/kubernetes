# 04. ConfigMap and Secret

## 범위

- 환경 변수 주입
- ConfigMap으로 일반 설정 분리
- Secret으로 민감 정보 분리

## 핵심 개념

- 애플리케이션 코드는 환경별 값과 분리하는 편이 낫다.
- ConfigMap은 일반 설정값 저장용이다.
- Secret은 비밀번호, 토큰 같은 민감값 저장용이다.
- Pod는 환경 변수나 파일 주입 방식으로 값을 받는다.

## 체크리스트

- [ ] Spring Boot에서 환경 변수 읽기
- [ ] ConfigMap 생성 및 주입
- [ ] Secret 생성 및 주입
- [ ] 매니페스트에서 `valueFrom` 구조 정리

## 실습 메모

- ConfigMap에서 넣은 값:
- Secret에서 넣은 값:
- Pod 내부에서 확인한 방식:
- 실수하기 쉬운 포인트:

