# kubernetes

Spring Boot, MySQL, Kubernetes, AWS 실습 내용을 리소스 단위로 정리한 저장소입니다.

## 한 장 요약

<img src="https://kubernetes.io/images/docs/components-of-kubernetes.svg" alt="Official Kubernetes components diagram" width="920" />

공식 Kubernetes 구성도  
출처: [Kubernetes Docs - Components of Kubernetes](https://kubernetes.io/docs/concepts/overview/components/)

```text
+----------------------------------+      +----------------------------------+
| kubernetes-backend/              | ---> | kubernetes-manifests/            |
| Spring Boot code                 |      | Deployment / Service             |
| Gradle build                     |      | ConfigMap / Secret               |
| Docker image                     |      | image: kube-ecr:1.0              |
+----------------------------------+      +----------------+-----------------+
                                                          |
                                                          v
                                         +----------------------------------+
                                         | Kubernetes cluster               |
                                         | backend-app Pods                 |
                                         | Service / NodePort               |
                                         +----------------+-----------------+
                                                          |
                                                          v
                                         +----------------------------------+
                                         | mysql-project/                   |
                                         | MySQL Deployment                 |
                                         | Service / PV / PVC              |
                                         +----------------------------------+

+----------------------------------+      +----------------------------------+
| aws/                             |      | study/                           |
| EC2 + k3s                        |      | 개념 정리                        |
| Nginx NodePort practice          |      | 실습 메모                        |
+----------------------------------+      +----------------------------------+

+----------------------------------+
| demo/                            |
| 초기 단일 폴더 예제              |
+----------------------------------+
```

## 무엇을 공부했는가

| 리소스 | 핵심 이해 | 저장소 위치 |
| --- | --- | --- |
| Pod | 컨테이너를 감싸는 가장 작은 배포 단위 | `demo/`, `study/` |
| Deployment | 복제본 유지, 롤링 업데이트, 자동 복구 | `kubernetes-manifests/`, `demo/` |
| Service | Pod 앞단의 고정 진입점 | `kubernetes-manifests/`, `aws/`, `demo/` |
| ConfigMap | 일반 설정값 분리 | `kubernetes-manifests/`, `demo/` |
| Secret | DB 계정/비밀번호 같은 민감값 분리 | `kubernetes-manifests/`, `demo/` |
| PV / PVC | MySQL 데이터 유지용 스토리지 연결 | `mysql-project/` |
| Docker image | Spring Boot 앱을 이미지로 패키징 | `kubernetes-backend/` |
| ECR | 빌드한 이미지를 레지스트리에 저장 | `kubernetes-backend/`, `study/` |
| k3s | EC2에서 가볍게 쿠버네티스 실습 | `aws/`, `study/` |
| EKS | 관리형 Kubernetes 개념 정리 | `study/` |

## 리소스별 정리

### Pod

- Pod는 쿠버네티스에서 컨테이너를 실행하는 가장 작은 단위입니다.
- 같은 Pod 안의 컨테이너는 네트워크와 스토리지를 함께 사용할 수 있습니다.
- 이 저장소에서는 Nginx, Spring Boot 예제로 Pod 개념을 먼저 익히는 흐름을 다룹니다.

### Deployment

- Deployment는 Pod를 직접 여러 개 관리하는 상위 리소스입니다.
- replicas로 개수를 맞추고, 장애가 나면 새 Pod를 다시 만듭니다.
- 이미지가 바뀌면 롤링 업데이트로 교체할 수 있습니다.

### Service

- Service는 Pod가 바뀌어도 변하지 않는 접근 지점을 제공합니다.
- ClusterIP는 클러스터 내부 통신용이고, NodePort는 외부 접근 실습에 자주 씁니다.
- 이 저장소에서는 Spring 앱과 Nginx 예제에서 Service 흐름을 같이 봅니다.

```text
+-------------+     +------------------+     +------------------+
| User/Client | --> | Service/NodePort | --> | backend-app Pods |
+-------------+     +------------------+     +------------------+
```

### ConfigMap / Secret

- ConfigMap은 일반 설정값을 분리할 때 사용합니다.
- Secret은 비밀번호, 계정 같은 민감값을 분리할 때 사용합니다.
- Spring Boot 앱은 환경 변수로 DB 접속 정보를 받아오도록 정리했습니다.

### PV / PVC

- PV는 실제 저장소 자원입니다.
- PVC는 Pod가 저장소를 요청하는 방식입니다.
- MySQL은 재시작해도 데이터가 유지돼야 하므로 PV/PVC 구성이 중요합니다.

### Docker / ECR

- `kubernetes-backend/`에서 Spring Boot 앱을 빌드하고 Docker 이미지로 만듭니다.
- 현재 ECR 배포 이미지는 `124244584275.dkr.ecr.ap-northeast-2.amazonaws.com/kube-ecr:1.0`입니다.
- 매니페스트도 `1.0` 태그 기준으로 맞춰 두었습니다.

### AWS / k3s / EKS

- `aws/`는 EC2 위 `k3s`에서 Nginx를 띄우는 최소 실습 예제입니다.
- NodePort로 외부 접근하는 흐름을 빠르게 확인할 수 있습니다.
- EKS는 저장소의 실습 노트에서 구조 중심으로 따로 정리해 두었습니다.

## 디렉터리 역할

| 디렉터리 | 역할 |
| --- | --- |
| `kubernetes-backend/` | Spring Boot 백엔드 소스, Gradle 빌드, Dockerfile |
| `kubernetes-manifests/` | Spring 앱용 Deployment, Service, ConfigMap, Secret |
| `mysql-project/` | MySQL Deployment, Service, PV, PVC |
| `aws/` | EC2 + `k3s` + Nginx 실습 |
| `study/` | 쿠버네티스 개념 정리와 학습 메모 |
| `demo/` | 초기에 만든 단일 폴더 예제 |

## 자주 쓰는 명령

```bash
cd kubernetes-backend
./gradlew clean build
docker build -t kube-ecr:1.0 .
docker tag kube-ecr:1.0 124244584275.dkr.ecr.ap-northeast-2.amazonaws.com/kube-ecr:1.0
docker push 124244584275.dkr.ecr.ap-northeast-2.amazonaws.com/kube-ecr:1.0
```

```bash
kubectl apply -f mysql-project/
kubectl apply -f kubernetes-manifests/
kubectl get pods
kubectl get svc
kubectl logs <pod-name>
```

```bash
kubectl apply -f aws/
kubectl get deploy
kubectl get pods
kubectl get svc
```
