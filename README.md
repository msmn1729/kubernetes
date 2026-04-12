# kubernetes

Spring Boot, MySQL, Kubernetes, AWS 배포 구성을 정리한 저장소.

## 구성 개요

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
                                         | Service / PV / PVC               |
                                         +----------------------------------+

+----------------------------------+      +----------------------------------+
| aws/                             |      | study/                           |
| EC2 + k3s                        |      | resource notes                   |
| Nginx NodePort                   |      | ops memo                         |
+----------------------------------+      +----------------------------------+

+----------------------------------+
| demo/                            |
| initial single-folder example    |
+----------------------------------+
```

## 핵심 리소스

| 리소스 | 역할 | 위치 |
| --- | --- | --- |
| Pod | 컨테이너 실행 단위 | `demo/`, `study/` |
| Deployment | 복제본 관리, 롤링 업데이트, 자동 복구 | `kubernetes-manifests/`, `demo/` |
| Service | Pod 앞단 고정 진입점 제공 | `kubernetes-manifests/`, `aws/`, `demo/` |
| ConfigMap | 일반 설정 분리 | `kubernetes-manifests/`, `demo/` |
| Secret | 민감 정보 분리 | `kubernetes-manifests/`, `demo/` |
| PV / PVC | 상태 저장 워크로드 스토리지 연결 | `mysql-project/` |
| Docker image | 애플리케이션 패키징 | `kubernetes-backend/` |
| ECR | 이미지 저장소 | `kubernetes-backend/`, `study/` |
| k3s | 경량 Kubernetes 배포 환경 | `aws/`, `study/` |
| EKS | 관리형 Kubernetes 구성 | `study/` |

## 리소스별 포인트

### Pod

- 컨테이너 실행 기본 단위
- 네트워크, 스토리지 네임스페이스 공유
- Nginx, Spring Boot 예제로 구성 확인
- Pod 는 직접 관리 대상이라기보다 Deployment 같은 상위 리소스로 다루는 흐름이 실무에서 더 자연스럽다.

### Deployment

- replicas 기준 Pod 수 유지
- 장애 발생 시 재생성
- 이미지 변경 시 롤링 업데이트 적용

### Service

- Pod 교체와 무관한 고정 접근 지점 제공
- ClusterIP: 클러스터 내부 통신
- NodePort: 외부 접근 실습

```text
+-------------+     +------------------+     +------------------+
| User/Client | --> | Service/NodePort | --> | backend-app Pods |
+-------------+     +------------------+     +------------------+
```

### ConfigMap / Secret

- ConfigMap: 일반 설정값 관리
- Secret: 계정, 비밀번호, 토큰 관리
- Spring Boot 환경 변수 주입 구조 적용

### PV / PVC

- PV: 실제 저장소 자원
- PVC: 저장소 요청 인터페이스
- MySQL 데이터 유지 구성에 사용

### Docker / ECR

- `kubernetes-backend/`에서 Gradle 빌드 후 이미지 생성
- ECR 배포 이미지: `124244584275.dkr.ecr.ap-northeast-2.amazonaws.com/kube-ecr:1.0`
- 매니페스트 이미지 태그 `1.0` 기준 정렬

### AWS / k3s / EKS

- `aws/`: EC2 + `k3s` + Nginx NodePort 예제
- 보안 그룹, NodePort, 이미지 배포 흐름 확인
- EKS 관련 메모는 `study/`에 분리

## 디렉터리 맵

| 디렉터리 | 용도 |
| --- | --- |
| `kubernetes-backend/` | Spring Boot 소스, Gradle 빌드, Dockerfile |
| `kubernetes-manifests/` | Spring Deployment, Service, ConfigMap, Secret |
| `mysql-project/` | MySQL Deployment, Service, PV, PVC |
| `aws/` | EC2 + `k3s` 배포 예제 |
| `study/` | 리소스 메모, 구성 정리 |
| `demo/` | 초기 단일 폴더 예제 |

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
