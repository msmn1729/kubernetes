# kubernetes

쿠버네티스 기본 개념과 흐름을 익히기 위해 정리한 실습 저장소다.

## 구조

```text
[Deployment]
   |
   v
[ReplicaSet]
   |
   v
[Pod]
```

```text
[사용자]
   |
   v
[Service]
   |
   v
[Pod]
```

```text
[ConfigMap / Secret]
          |
          v
        [Pod]
```

```text
[Pod]
  |
  v
[PVC]
  |
  v
[PV]
  |
  v
[Storage]
```

## 익힌 것

- Deployment가 Pod를 직접 관리하는 게 아니라 ReplicaSet을 통해 관리한다.
- Service는 Pod 앞단에서 트래픽을 받는 진입점이다.
- ClusterIP는 클러스터 내부 통신용이고 NodePort는 외부 접속용이다.
- ConfigMap과 Secret으로 설정값을 Pod에 넣을 수 있다.
- PVC와 PV를 사용하면 Pod가 다시 떠도 데이터를 유지할 수 있다.

## 실습에 사용한 것

- Spring Boot
- MySQL
- Docker Desktop Kubernetes

## 폴더

```text
demo
-> Spring Boot 앱과 Kubernetes 매니페스트

mysql-project
-> MySQL, Service, PV/PVC 매니페스트
```
