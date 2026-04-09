# 09. Monthly Kubernetes Summary

## 이번 구간에서 잡은 큰 흐름

```text
+-------------------+     +--------------------+     +-------------------+
| Local Kubernetes  | --> | Spring + MySQL     | --> | AWS deployment    |
| Docker Desktop    |     | manifests split    |     | EC2 / k3s / ECR   |
+-------------------+     +--------------------+     +-------------------+
         |                            |                           |
         v                            v                           v
+-------------------+     +--------------------+     +-------------------+
| Pod basics        |     | ConfigMap/Secret   |     | RDS / image push  |
| Deployment        |     | PV / PVC           |     | NodePort access   |
+-------------------+     +--------------------+     +-------------------+
```

## 지금까지 실제로 손댄 축

- `demo/`: 초반에 Spring 관련 매니페스트를 한 폴더에서 빠르게 실습
- `mysql-project/`: MySQL Deployment, Service, PV, PVC 분리
- `kubernetes-backend/`: Spring Boot 게시판 예제 소스와 Dockerfile 분리
- `kubernetes-manifests/`: Spring Deployment, Service, ConfigMap, Secret 분리
- `aws/`: EC2 `k3s`에서 Nginx를 띄우는 최소 배포 예제

## 정리된 개념 축

- Pod
  Nginx와 Spring Boot를 직접 띄우면서 가장 작은 배포 단위를 확인
- Deployment
  replica 유지, self-healing, rolling update 관점으로 이해
- Service
  Pod 앞단 진입점과 NodePort 접근 흐름 정리
- ConfigMap / Secret
  환경 변수와 민감값을 코드에서 분리
- PV / PVC
  MySQL 데이터를 컨테이너 생명주기와 분리
- EC2 / k3s
  로컬과 다른 배포 환경 요소를 확인
- ECR / RDS
  애플리케이션 이미지 배포와 외부 DB 연결 흐름으로 확장
- EKS
  관리형 Kubernetes 구조를 다음 단계 학습 대상으로 확보

## 지금 레포에서 바로 이어서 할 수 있는 실습

- `kubernetes-backend/`를 실제로 빌드하고 이미지 태그 규칙 정리
- `kubernetes-manifests/`에 이미지 이름, resource 설정, readiness/liveness probe 추가
- `mysql-project/`에 실제 접속 검증 절차를 문서화
- `aws/` 실습을 Spring Boot 배포 흐름까지 확장
- `study/_daily-template.md`를 복제해 매번 실습 기록 남기기

## 다음 공부 우선순위

1. Spring Boot + MySQL 로컬 쿠버 연동을 끝까지 재현
2. ECR push와 k3s pull 권한 흐름을 실제로 한 번 더 정리
3. RDS 연결 시 환경 변수와 Secret 구조를 매니페스트에 고정
4. EKS는 마지막에 구조 이해 중심으로 정리

## 한 줄 요약

지금 레포는 쿠버네티스 개념 정리 단계는 지나서, `Spring Boot 앱 + MySQL + AWS 배포`를 반복하면서 체득하는 단계로 넘어가고 있다.
