# kubernetes

쿠버네티스 학습 내용을 직접 실습하면서 정리하는 저장소입니다.

## 구조 한눈에 보기

```text
+--------------------------------------------------------------+
|                        kubernetes                            |
|             Kubernetes study workspace overview              |
+--------------------------------------------------------------+
|                                                              |
|  +----------------------+     +---------------------------+  |
|  | kubernetes-backend/  |     | kubernetes-manifests/    |  |
|  | - Spring Boot app    | --> | - Deployment             |  |
|  | - Board API          |     | - Service                |  |
|  | - Dockerfile         |     | - ConfigMap / Secret     |  |
|  +----------------------+     +---------------------------+  |
|                 |                            |                |
|                 +------------ build/apply ---+                |
|                                              v                |
|                           +---------------------------+      |
|                           | Kubernetes cluster        |      |
|                           | - backend-app Pods        |      |
|                           | - NodePort / ClusterIP    |      |
|                           +---------------------------+      |
|                                              |                |
|                                              v                |
|                           +---------------------------+      |
|                           | mysql-project/            |      |
|                           | - MySQL Deployment        |      |
|                           | - Service                 |      |
|                           | - PV / PVC                |      |
|                           +---------------------------+      |
|                                                              |
|  +----------------------+                                    |
|  | aws/                 | --> EC2 + k3s + nginx practice     |
|  | - deployment.yaml    | --> external access via NodePort   |
|  | - service.yaml       |                                    |
|  +----------------------+                                    |
|                                                              |
|  +----------------------+                                    |
|  | study/               | --> curriculum-based study notes   |
|  | - roadmap            | --> topic-by-topic practice log    |
|  | - daily template     |                                    |
|  +----------------------+                                    |
|                                                              |
|  +----------------------+                                    |
|  | demo/                | --> earlier single-folder example  |
|  +----------------------+                                    |
|                                                              |
+--------------------------------------------------------------+
```

## 구성 요약

- `kubernetes-backend/`: Spring Boot 게시판 예제 소스와 `Dockerfile`
- `kubernetes-manifests/`: Spring 앱용 Deployment, Service, ConfigMap, Secret
- `mysql-project/`: MySQL Deployment, Service, PV, PVC
- `aws/`: EC2 `k3s` 환경에서 Nginx를 띄우는 최소 실습 예제
- `study/`: 강의 커리큘럼 기준으로 쪼갠 학습 노트와 기록 템플릿
- `demo/`: 초기에 정리한 단일 폴더형 예제

## 디렉터리

```text
.
├─ aws/
│  ├─ deployment.yaml
│  └─ service.yaml
├─ demo/
│  ├─ spring-deployment.yaml
│  ├─ spring-service.yaml
│  ├─ spring-config.yaml
│  ├─ spring-secret.yaml
│  └─ Dockerfile
├─ kubernetes-backend/
│  ├─ Dockerfile
│  └─ src/main/
│     ├─ java/com/example/demo/
│     └─ resources/application.yml
├─ kubernetes-manifests/
│  ├─ spring-deployment.yaml
│  ├─ spring-service.yaml
│  ├─ spring-config.yaml
│  └─ spring-secret.yaml
├─ study/
│  ├─ README.md
│  ├─ 01-kubernetes-overview.md
│  ├─ 02-pod-nginx-spring.md
│  ├─ 03-deployment-service.md
│  ├─ 04-configmap-secret.md
│  ├─ 05-volume-mysql.md
│  ├─ 06-ec2-k3s-nginx.md
│  ├─ 07-spring-rds-ecr-k3s.md
│  ├─ 08-eks-basics.md
│  └─ _daily-template.md
└─ mysql-project/
   ├─ mysql-deployment.yaml
   ├─ mysql-service.yaml
   ├─ mysql-config.yaml
   ├─ mysql-secret.yaml
   ├─ mysql-pv.yaml
   └─ mysql-pvc.yaml
```

## 최근 한 달 학습 요약

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

- 로컬 Docker Desktop Kubernetes로 개념과 명령어 흐름을 익힘
- Spring Boot 앱과 MySQL을 분리된 매니페스트 구조로 정리
- ConfigMap, Secret, PV, PVC를 통해 앱 설정과 데이터 저장소 흐름을 분리
- EC2 `k3s` 환경에서 Nginx NodePort 실습을 진행
- Spring Boot + ECR + RDS + `k3s` 배포 흐름을 다음 실습 축으로 잡음
- EKS는 관리형 Kubernetes 구조를 이해하는 다음 단계 주제로 확보

## 다음 공부 우선순위

1. Spring Boot + MySQL 로컬 쿠버 연동을 끝까지 재현
2. ECR push와 `k3s` pull 권한 흐름을 실제로 다시 정리
3. RDS 연결 시 환경 변수와 Secret 구조를 매니페스트에 고정
4. EKS는 마지막에 구조 이해 중심으로 정리

## 실행 흐름

```text
+-------------+      +--------------------+      +--------------------+
| User/Client | ---> | Service / NodePort | ---> | backend-app Pods   |
+-------------+      +--------------------+      +--------------------+
                                                          |
                                                          v
                                                +--------------------+
                                                | ConfigMap / Secret |
                                                +--------------------+
                                                          |
                                                          v
                                                +--------------------+
                                                | MySQL + PV / PVC   |
                                                +--------------------+
```

## AWS k3s 실습

```bash
kubectl apply -f aws/
```

```bash
kubectl get deploy
kubectl get pods
kubectl get svc
```

```text
http://EC2_PUBLIC_IP:30000
```

- EC2 보안 그룹에서 `30000/TCP` 인바운드 허용 필요
- ARM 인스턴스(`t4g.small`)에서는 설치 명령이나 이미지 아키텍처를 확인해야 함

## 자주 쓰는 kubectl

```bash
kubectl apply -f .
kubectl delete -f .
kubectl get pods
kubectl get deploy
kubectl get svc
kubectl describe pod <pod-name>
kubectl logs <pod-name>
kubectl exec -it <pod-name> -- sh
kubectl rollout restart deployment <deployment-name>
```
