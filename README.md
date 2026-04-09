# kubernetes

Spring Boot, MySQL, Kubernetes, AWS 실습 내용을 모아둔 저장소입니다.

## 구조

<img src="https://kubernetes.io/images/docs/components-of-kubernetes.svg" alt="Official Kubernetes components diagram" width="980" />

공식 Kubernetes 구성도  
출처: [Kubernetes Docs - Components of Kubernetes](https://kubernetes.io/docs/concepts/overview/components/)

## 저장소 흐름

```text
+----------------------------------+
| kubernetes-backend/              |
| - Spring Boot source             |
| - Gradle build                   |
| - Docker image                   |
+----------------+-----------------+
                 |
                 v
+----------------------------------+
| kubernetes-manifests/            |
| - Deployment                     |
| - Service                        |
| - ConfigMap / Secret             |
+----------------+-----------------+
                 |
                 v
+----------------------------------+
| Kubernetes cluster               |
| - backend-app Pods               |
| - Service / NodePort             |
+----------------+-----------------+
                 |
                 v
+----------------------------------+
| mysql-project/                   |
| - MySQL Deployment               |
| - Service                        |
| - PV / PVC                       |
+----------------------------------+

+----------------------------------+   +----------------------------------+
| aws/                             |   | study/                           |
| - EC2 + k3s                      |   | - Kubernetes notes               |
| - Nginx / NodePort               |   | - practice records               |
+----------------------------------+   +----------------------------------+

+----------------------------------+
| demo/                            |
| - initial single-folder example  |
+----------------------------------+
```

## 핵심 포인트

- 백엔드 코드는 `kubernetes-backend/`에 모여 있습니다.
- 배포 설정은 `kubernetes-manifests/`와 `mysql-project/`로 분리돼 있습니다.
- AWS 실습 파일은 `aws/`에 따로 모아뒀습니다.
- 학습 정리는 `study/`, 초기 예제는 `demo/`에 있습니다.
- ECR 배포 이미지는 `124244584275.dkr.ecr.ap-northeast-2.amazonaws.com/kube-ecr:1.0`을 사용합니다.

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
```

```bash
kubectl apply -f aws/
kubectl get deploy
kubectl get pods
kubectl get svc
```
