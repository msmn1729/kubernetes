# kubernetes

Docker Desktop Kubernetes에서 Spring Boot와 MySQL을 붙여본 실습 기록이다.

## 구조

```text
[사용자]
   |
   v
[spring-service : 30000]
   |
   v
[spring pod x3]
   |
   v
[mysql-service : 30002]
   |
   v
[mysql pod x1]
   |
   v
[PVC]
   |
   v
[PV]
```

## 핵심

```text
ClusterIP
-> 쿠버 내부 통신용

NodePort
-> 외부에서 붙기 위한 포트

Deployment
-> 파드 개수와 롤링업데이트 관리

ReplicaSet
-> 파드를 실제로 유지

PVC / PV
-> 파드가 다시 떠도 MySQL 데이터 유지
```

## 확인한 것

- Spring이 서비스 이름 `mysql-service`로 MySQL에 연결되는 흐름을 확인했다.
- NodePort, port, targetPort 차이를 확인했다.
- MySQL을 PVC/PV에 붙였을 때 파드 재기동 후 데이터가 남는 것을 확인했다.
- Docker Desktop Kubernetes에서 `EOF`, `cpuset` 문제를 복구했다.

## 실행

### MySQL

```powershell
cd mysql-project
kubectl apply -f .
```

### Spring

```powershell
cd demo
./gradlew.bat build
docker build -t spring-server .
kubectl apply -f .
```

## 폴더

```text
demo
-> Spring Boot 앱 + Kubernetes 매니페스트

mysql-project
-> MySQL + PV/PVC + Service 매니페스트
```
