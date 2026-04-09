# kubernetes

Spring Boot, MySQL, Kubernetes, AWS 실습 저장소입니다.

## 전체 구조

<img src="https://kubernetes.io/images/docs/components-of-kubernetes.svg" alt="Official Kubernetes components diagram" width="980" />

공식 Kubernetes 구성도  
출처: [Kubernetes Docs - Components of Kubernetes](https://kubernetes.io/docs/concepts/overview/components/)

## 이 저장소 흐름

```text
[kubernetes-backend]
  Spring Boot code
  Docker build
        |
        v
[kubernetes-manifests]
  Deployment / Service
  ConfigMap / Secret
        |
        v
[Kubernetes cluster]
  backend-app Pods
        |
        v
[mysql-project]
  MySQL
  PV / PVC

[aws]
  EC2 + k3s 실습

[study]
  학습 노트

[demo]
  초기 예제
```

## 폴더 역할

- `kubernetes-backend/`: Spring Boot 백엔드 코드
- `kubernetes-manifests/`: 백엔드 배포 매니페스트
- `mysql-project/`: MySQL 배포와 볼륨 설정
- `aws/`: EC2 + `k3s` 실습 파일
- `study/`: 학습 정리
- `demo/`: 초기 실습 예제

## 자주 쓰는 명령

```bash
cd kubernetes-backend
./gradlew clean build
docker build -t kube-ecr:1.0 .
```

```bash
kubectl apply -f mysql-project/
kubectl apply -f kubernetes-manifests/
kubectl get pods
kubectl get svc
```
