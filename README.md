# kubernetes

Spring Boot, MySQL, Kubernetes, AWS 배포를 공부하며 정리한 저장소.

## 구성 개요

<img src="https://kubernetes.io/images/docs/components-of-kubernetes.svg" alt="Kubernetes 구성도" width="920" />

공식 Kubernetes 구성도  
출처: [Kubernetes Docs - Components of Kubernetes](https://kubernetes.io/docs/concepts/overview/components/)

```text
+----------------------------------+      +----------------------------------+
| kubernetes-backend/              | ---> | kubernetes-manifests/            |
| Spring Boot 코드                 |      | Deployment / Service             |
| Gradle 빌드                      |      | ConfigMap / Secret               |
| Docker 이미지                    |      | image: kube-ecr:1.0              |
+----------------------------------+      +----------------+-----------------+
                                                          |
                                                          v
                                         +----------------------------------+
                                         | Kubernetes 클러스터              |
                                         | backend-app 파드들               |
                                         | Service / NodePort               |
                                         +----------------+-----------------+
                                                          |
                                                          v
                                         +----------------------------------+
                                         | mysql-project/                   |
                                         | MySQL 배포                       |
                                         | Service / PV / PVC               |
                                         +----------------------------------+

+----------------------------------+      +----------------------------------+
| aws/                             |      | study/                           |
| EC2 + k3s                        |      | 리소스 메모                      |
| Nginx 배포                       |      | 정리 문서                        |
+----------------------------------+      +----------------------------------+

+----------------------------------+
| demo/                            |
| 초기 한 폴더 예제                |
+----------------------------------+
```

## 핵심 리소스

| 리소스 | 역할 | 위치 |
| --- | --- | --- |
| Pod | 컨테이너를 실행하는 가장 작은 단위 | `demo/`, `study/` |
| Deployment | 파드 개수 유지, 업데이트, 장애 복구 | `kubernetes-manifests/`, `demo/` |
| Service | 파드 앞에서 고정 주소 역할 | `kubernetes-manifests/`, `aws/`, `demo/` |
| ConfigMap | 일반 설정값 분리 | `kubernetes-manifests/`, `demo/` |
| Secret | 비밀번호 같은 민감값 분리 | `kubernetes-manifests/`, `demo/` |
| ServiceAccount (SA) | 파드가 쿠버네티스 API를 쓸 때 사용하는 계정 | `study/` |
| CRD | 클러스터에 새 리소스 종류를 등록하는 설정 | `study/` |
| CR | CRD로 만든 실제 리소스 데이터 | `study/` |
| PV / PVC | 저장 공간 연결 | `mysql-project/` |
| Docker 이미지 | 애플리케이션 포장 단위 | `kubernetes-backend/` |
| ECR | Docker 이미지 저장소 | `kubernetes-backend/`, `study/` |
| k3s | 가벼운 Kubernetes 배포 환경 | `aws/`, `study/` |
| EKS | AWS 관리형 Kubernetes 서비스 | `study/` |

## 리소스별 포인트

### Pod

- Pod 는 컨테이너를 띄우는 가장 작은 단위다.
- Pod 안 컨테이너들은 네트워크와 저장소를 함께 쓴다.
- Nginx, Spring Boot 예제로 기본 구성을 확인할 수 있다.
- 보통 Pod 를 하나씩 직접 관리하기보다 Deployment 로 관리한다.
- 같은 Pod 안 컨테이너끼리는 `localhost`로 통신할 수 있다.
- 컨테이너가 떠 있어도 바로 외부 접속이 되는 건 아니고, `describe`, `logs`, `exec`로 상태를 자주 확인한다.

### Deployment

- 원하는 수만큼 Pod 가 유지되게 관리한다.
- 장애가 나면 ReplicaSet 을 통해 Pod 를 다시 띄운다.
- 이미지가 바뀌면 새 Pod 를 먼저 올리고 기존 Pod 를 줄이며 업데이트한다.
- 즉 Deployment 는 "원하는 상태를 적어두는 관리자"처럼 이해하면 쉽다.

### Service

- Pod 가 바뀌어도 접속 주소가 바뀌지 않게 해 준다.
- `ClusterIP`는 클러스터 안에서 쓰고, `NodePort`는 바깥에서 접속할 때 쓴다.
- Service 이름이 DNS 로 등록되기 때문에 Pod 이름이나 IP 가 바뀌어도 안정적으로 붙을 수 있다.
- 애플리케이션끼리 통신할 때는 Pod 주소보다 Service 이름으로 보는 쪽이 훨씬 편하다.
- Service 가 여러 Pod 로 트래픽을 나눠 주기 때문에 replicas 개념과 같이 보면 이해가 쉽다.

```text
+-------------+     +------------------+     +--------------------+
| 사용자/클라이언트 | --> | Service/NodePort | --> | backend-app 파드들 |
+-------------+     +------------------+     +--------------------+
```

### ConfigMap / Secret

- ConfigMap 은 일반 설정값을 따로 빼두는 리소스다.
- Secret 은 계정, 비밀번호, 토큰 같은 민감한 값을 담는다.
- Pod 는 환경 변수나 파일 마운트 방식으로 이 값을 받아 쓴다.
- 코드와 설정값을 나누면 배포할 때 관리가 훨씬 편하다.

### ServiceAccount (SA)

- ServiceAccount 는 Pod 가 Kubernetes API 를 사용할 때 쓰는 계정이다.
- 사람이 `kubectl`로 접속할 때 쓰는 계정과는 다르게, 애플리케이션이나 컨트롤러용 계정이라고 보면 된다.
- 따로 지정하지 않으면 네임스페이스의 `default` ServiceAccount 를 쓴다.
- ServiceAccount 만 만든다고 권한이 생기지는 않고, `Role`, `RoleBinding` 같은 RBAC 과 연결돼야 한다.
- Ingress Controller 나 cert-manager 같은 도구가 API 서버를 읽고 쓸 때도 이 계정을 사용한다.

### CRD

- CRD 는 `CustomResourceDefinition` 의 줄임말이다.
- 기본 리소스 말고도 새로운 리소스 종류를 클러스터에 추가할 때 쓴다.
- 쉽게 말해 Kubernetes 에 새로운 `Kind` 를 등록하는 설정이다.
- CRD 에 필드 규칙을 넣어 두면 잘못된 값이 들어오는 걸 더 빨리 막을 수 있다.
- 다만 CRD 만 만든다고 바로 동작하는 건 아니고, 이를 보고 움직이는 컨트롤러나 오퍼레이터가 같이 있어야 한다.

### CR

- CR 은 `Custom Resource` 의 줄임말이다.
- CRD 로 만든 리소스 종류를 실제로 하나 생성한 객체가 CR 이다.
- CRD 가 양식이라면 CR 은 그 양식에 맞게 실제로 작성한 데이터라고 보면 된다.
- 예를 들어 `Certificate`, `Kafka`, `Prometheus` 같은 리소스를 만들 때 이 개념이 자주 나온다.
- 보통 CR 에 원하는 상태를 적어 두면, 컨트롤러나 오퍼레이터가 그걸 읽고 실제 리소스를 만들어 준다.

```text
CRD(새 리소스 타입 정의)
        |
        v
CR(실제 객체 생성)
        |
        v
Controller / Operator 가 감시
        |
        v
Deployment / StatefulSet / Service / Secret 같은 실제 리소스 생성
```

### PV / PVC

- PV 는 실제 저장 공간이다.
- PVC 는 그 저장 공간을 쓰겠다고 요청하는 리소스다.
- 이 저장 구조를 MySQL 데이터 유지에 사용한다.
- Pod 가 다시 만들어져도 같은 PVC 를 쓰면 데이터가 남는다.
- 상태 저장 앱은 Deployment 뿐 아니라 저장소 연결까지 같이 봐야 이해가 된다.

### Docker / ECR

- `kubernetes-backend/`에서 Gradle 빌드 후 이미지를 만든다.
- ECR 배포 이미지: `124244584275.dkr.ecr.ap-northeast-2.amazonaws.com/kube-ecr:1.0`
- 매니페스트도 같은 이미지 태그 `1.0` 기준으로 맞춘다.

### AWS / k3s / EKS

- `aws/`: EC2 + `k3s` + Nginx NodePort 예제
- 보안 그룹, NodePort, 이미지 배포 흐름을 같이 확인한다.
- EKS 관련 메모는 `study/`에 따로 정리했다.

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
kubectl get sa
kubectl get crd
kubectl logs <pod-name>
```

```bash
kubectl apply -f aws/
kubectl get deploy
kubectl get pods
kubectl get svc
```


## 추가로 정리한 포인트

- Pod 는 실행 단위, Deployment 는 관리 단위, Service 는 접속 단위로 나눠서 보면 덜 헷갈린다.
- Spring Boot 와 MySQL 연결은 앱 설정, Kubernetes 리소스, 네트워크를 같이 봐야 이해가 된다.
- 로컬 실습에서는 NodePort 로 바깥 접속 흐름을 보고, 내부 통신은 Service 이름으로 보는 방식이 가장 쉽다.
- ServiceAccount 는 "누가 API 를 쓰는가", Role/Binding 은 "어디까지 할 수 있는가"로 보면 정리가 잘 된다.
- CRD 는 리소스 종류 정의, CR 은 실제 데이터, 컨트롤러/오퍼레이터는 실제 상태를 맞추는 역할이라고 보면 된다.
