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
└─ mysql-project/
   ├─ mysql-deployment.yaml
   ├─ mysql-service.yaml
   ├─ mysql-config.yaml
   ├─ mysql-secret.yaml
   ├─ mysql-pv.yaml
   └─ mysql-pvc.yaml
```

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
