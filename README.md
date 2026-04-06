# Kubernetes Study Workspace

Docker Desktop Kubernetes 환경에서 `Spring Boot + MySQL` 구성을 실습한 기록용 저장소입니다.

## Included Projects

### `mysql-project`
- MySQL Deployment
- Service(NodePort)
- ConfigMap / Secret
- PV / PVC 기반 데이터 영속성 실습

### `demo`
- Spring Boot API
- Docker image build
- Kubernetes Deployment / Service
- ConfigMap / Secret 기반 DB 연결

## What I Verified

- `NodePort`, `ClusterIP`, `targetPort` 차이
- `Deployment -> ReplicaSet -> Pod` 관계
- MySQL을 PVC에 연결했을 때 파드 재기동 후 데이터 유지
- Spring Boot가 Kubernetes 내부 Service 이름으로 MySQL에 연결되는 흐름
- Docker Desktop Kubernetes 장애(`EOF`, `cpuset`) 복구 과정

## Run Order

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

## Notes

- 실습 환경은 Docker Desktop 단일 노드 Kubernetes 클러스터입니다.
- MySQL은 `hostPath` 기반 PV/PVC로 연결해 재기동 후 데이터 유지 여부를 확인했습니다.
- Spring은 `spring-config.yaml`, `spring-secret.yaml`을 통해 DB 연결 정보를 주입합니다.
