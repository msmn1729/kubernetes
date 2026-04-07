# kubernetes

쿠버네티스 학습 내용을 직접 실습하면서 정리하는 저장소입니다.

현재 포함된 내용:

- 로컬 Docker Desktop Kubernetes 실습
- MySQL PV/PVC 기반 구성
- Spring Boot + MySQL 연동용 매니페스트
- AWS EC2 위 `k3s` 실습용 Nginx Deployment/Service 매니페스트

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
└─ mysql-project/
   ├─ mysql-deployment.yaml
   ├─ mysql-service.yaml
   ├─ mysql-config.yaml
   ├─ mysql-secret.yaml
   ├─ mysql-pv.yaml
   └─ mysql-pvc.yaml
```

## 큰 그림

### 1. Deployment

Deployment는 Pod를 직접 띄우는 매니페스트이면서, 동시에 Pod 개수 유지와 재생성, 업데이트를 관리합니다.

```text
Deployment
    |
    v
ReplicaSet
    |
    v
Pod
    |
    v
Container
```

### 2. Service

Service는 Pod 앞단에서 고정된 진입점을 제공합니다.

- `NodePort`: 외부에서 `노드IP:nodePort`로 접근
- `ClusterIP`: 클러스터 내부 통신 전용

포트 개념은 이렇게 정리합니다.

- `nodePort` = 외부포트
- `port` = 클러스터 내부에서 Service 입구포트
- `targetPort` = Pod 입구포트

```text
외부 사용자
   |
   | EC2_IP:30000
   v
NodePort 30000
   |
   v
Service port 80
   |
   v
Pod targetPort 80
```

### 3. ConfigMap / Secret

- `ConfigMap`: 일반 설정값
- `Secret`: 민감한 값

Pod는 이 값을 환경 변수나 파일로 주입받아 사용합니다.

### 4. PV / PVC

- `PV`: 실제 스토리지 자원
- `PVC`: Pod가 요청하는 스토리지 요청서

MySQL 같은 상태 저장 워크로드에서 데이터 유지를 위해 사용합니다.

## AWS k3s 실습

`aws/` 디렉터리는 EC2 위 `k3s` 환경에서 Nginx를 띄우는 가장 기본적인 실습 예제입니다.

포함 리소스:

- `deployment.yaml`: `nginx` Pod 3개를 관리하는 Deployment
- `service.yaml`: 외부 접근용 NodePort Service

적용:

```bash
kubectl apply -f aws/
```

확인:

```bash
kubectl get deploy
kubectl get pods
kubectl get svc
```

외부 접근:

```text
http://EC2_PUBLIC_IP:30000
```

주의:

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

## 메모

- 로컬에서는 빠르게 구조를 익히고
- AWS에서는 실제 서버 배포 흐름을 익히는 용도로 분리해서 학습
- `k9s`는 운영/조회에 편하지만, `kubectl` 기본기는 반드시 같이 익혀야 함
