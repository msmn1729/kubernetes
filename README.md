# kubernetes

쿠버네티스에서 자주 나오는 구조와 흐름을 짧게 정리한 실습 저장소다.

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

- Deployment는 Pod를 직접 관리하지 않고 ReplicaSet을 통해 관리한다.
- Service는 Pod 앞에서 요청을 받아서 연결해주는 역할을 한다.
- ClusterIP는 클러스터 내부 통신용이고 NodePort는 외부에서 붙을 때 쓴다.
- ConfigMap과 Secret으로 설정값을 Pod에 넣을 수 있다.
- PVC와 PV를 사용하면 Pod가 다시 떠도 데이터를 유지할 수 있다.
