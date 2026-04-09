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

## 학습 트랙

- [study/README.md](study/README.md): 전체 로드맵과 학습 순서
- [01-kubernetes-overview.md](study/01-kubernetes-overview.md): 쿠버네티스 개념, Docker Desktop 로컬 셋업
- [02-pod-nginx-spring.md](study/02-pod-nginx-spring.md): Pod, Nginx, Spring Boot, 이미지 풀 정책
- [03-deployment-service.md](study/03-deployment-service.md): Deployment, Service, scaling, self-healing, rolling update
- [04-configmap-secret.md](study/04-configmap-secret.md): 환경 변수 분리, ConfigMap, Secret
- [05-volume-mysql.md](study/05-volume-mysql.md): Volume, PV/PVC, MySQL 연동
- [06-ec2-k3s-nginx.md](study/06-ec2-k3s-nginx.md): EC2, k3s, Nginx NodePort 실습
- [07-spring-rds-ecr-k3s.md](study/07-spring-rds-ecr-k3s.md): Spring Boot 빌드, ECR push, RDS, k3s 배포
- [08-eks-basics.md](study/08-eks-basics.md): EKS 기본 구조와 배포 흐름
- [_daily-template.md](study/_daily-template.md): 실제 공부할 때 복제해서 쓰는 기록 템플릿

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
