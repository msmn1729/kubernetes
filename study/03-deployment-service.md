# 03. Deployment and Service

## 범위

- Deployment 개념
- Service 개념
- scale out, self-healing, rolling update

## 핵심 개념

- Deployment는 원하는 Pod 개수를 유지한다.
- Pod가 죽으면 ReplicaSet을 통해 자동으로 다시 생성된다.
- Service는 Pod 앞에 고정된 진입점을 만든다.
- NodePort는 외부 접근, ClusterIP는 내부 통신용으로 자주 쓴다.

## 체크리스트

- [ ] Spring Boot Deployment 3개 띄우기
- [ ] Service로 Pod와 통신 확인
- [ ] replica 수 변경
- [ ] Pod 삭제 후 self-healing 확인
- [ ] 이미지 변경 후 rolling update 확인

## 시각 흐름

```text
+------------+      +------------+      +------+
| Deployment | ---> | ReplicaSet | ---> | Pods |
+------------+      +------------+      +------+
```

```text
+--------+      +----------------+      +------+
| Client | ---> | Service        | ---> | Pods |
|        |      | ClusterIP/Port |      |      |
+--------+      +----------------+      +------+
```

## 실습 메모

- replica 변경 결과:
- self-healing 확인 방법:
- rolling update 중 관찰한 점:

