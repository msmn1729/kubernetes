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

- 컨테이너 실행 기본 단위, 하나 이상의 컨테이너를 감싸는 가장 작은 배포 단위
- 네트워크, 스토리지 네임스페이스 공유
- Nginx, Spring Boot 예제로 구성 확인
- Pod 는 직접 하나씩 관리하기보다 상위 리소스인 Deployment 기준으로 다루는 흐름이 더 자연스럽다.
- 같은 Pod 안 컨테이너들은 localhost 기준으로 통신할 수 있어서 사이드카 패턴 이해에도 연결된다.
- 컨테이너가 떠 있어도 외부 접속이 바로 되는 것은 아니며, describe, logs, exec 는 디버깅의 기본이다.

### Deployment

- replicas 기준 Pod 수 유지, 원하는 상태를 선언하는 관리자 역할
- 장애 발생 시 ReplicaSet 을 통해 자동으로 Pod 재생성 (self-healing)
- 이미지 변경 시 롤링 업데이트 적용으로 무중단 배포 가능
- 실제로는 ReplicaSet 을 통해 Pod 가 유지되므로, Deployment 는 원하는 상태를 선언하는 관리자에 가깝다.
- 이미지 태그를 바꾸면 새 버전 Pod 를 순차적으로 올리고 기존 Pod 를 줄이는 식으로 업데이트가 진행된다.

### Service

- Pod 교체와 무관한 고정 접근 지점 제공
- ClusterIP: 클러스터 내부 통신, NodePort: 외부 접근으로 용도별 선택 가능
- Service 이름으로 DNS 자동 등록되어 Pod 이름이나 IP 변경과 무관하게 안정적 통신
- Pod IP 는 바뀔 수 있으므로 애플리케이션 간 통신은 Pod 주소보다 Service 이름 기준으로 이해하는 편이 훨씬 안정적이다.
- Service 가 트래픽을 여러 Pod 로 분산해 주기 때문에 replicas 증가와 연결해서 보면 이해가 더 잘 된다.

```text
+-------------+     +------------------+     +------------------+
| User/Client | --> | Service/NodePort | --> | backend-app Pods |
+-------------+     +------------------+     +------------------+
```

### ConfigMap / Secret

- ConfigMap: 일반 설정값 관리, 애플리케이션 코드와 환경 값 분리
- Secret: 계정, 비밀번호, 토큰 같은 민감값 관리 및 암호화 저장
- Pod는 환경 변수 또는 파일 마운트 방식으로 ConfigMap과 Secret 값을 주입받음
- Spring Boot 환경 변수 주입 구조 적용으로 설정 관리 자동화

### PV / PVC

- PV: 실제 저장소 자원
- PVC: 저장소 요청 인터페이스
- MySQL 데이터 유지 구성에 사용
- Pod 가 재생성되어도 PVC 를 통해 같은 저장소에 다시 연결되면 데이터가 유지된다.
- 상태 저장 워크로드는 Deployment 만 보는 것보다 스토리지 연결 방식까지 같이 봐야 흐름이 잡힌다.

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


## 추가로 정리한 이해 포인트

- Pod 는 실행 단위, Deployment 는 관리 단위, Service 는 접근 단위로 나눠서 보면 헷갈림이 줄어든다.
- Spring Boot 와 MySQL 연결은 결국 애플리케이션 설정, Kubernetes 리소스, 네트워크 개념이 같이 맞물려야 이해된다.
- 로컬 실습에서는 NodePort 로 외부 접근 흐름을 확인하고, 내부 통신은 Service 이름으로 보는 방식이 가장 이해하기 쉬웠다.
