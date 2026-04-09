# Kubernetes Study Track

강의 커리큘럼을 기준으로 실제 실습과 노트 작성을 이어가기 위한 학습 인덱스입니다.

## 로드맵

| 순서 | 파일 | 주제 |
| --- | --- | --- |
| 1 | `01-kubernetes-overview.md` | 쿠버네티스 개념, Docker Desktop 로컬 환경 |
| 2 | `02-pod-nginx-spring.md` | Pod, Nginx, Spring Boot, 이미지 풀 정책 |
| 3 | `03-deployment-service.md` | Deployment, Service, scale, self-healing |
| 4 | `04-configmap-secret.md` | ConfigMap, Secret, 환경 변수 분리 |
| 5 | `05-volume-mysql.md` | Volume, PV/PVC, MySQL 연동 |
| 6 | `06-ec2-k3s-nginx.md` | EC2, k3s, Nginx 배포 |
| 7 | `07-spring-rds-ecr-k3s.md` | ECR, RDS, Spring Boot 배포 |
| 8 | `08-eks-basics.md` | EKS 구조, 워커 노드, 로컬 연동 |

## 사용 방식

1. 학습할 주제 파일을 열고 체크리스트를 진행합니다.
2. 실습하면서 바뀐 매니페스트, 명령어, 이슈를 파일 하단에 적습니다.
3. 하루 공부가 끝나면 `_daily-template.md`를 복제해서 별도 기록을 남깁니다.
4. 실제 변경이 생길 때마다 커밋합니다.

## 현재 레포와 연결되는 위치

- `kubernetes-backend/`: Spring Boot 실습 소스
- `kubernetes-manifests/`: Deployment, Service, ConfigMap, Secret
- `mysql-project/`: MySQL + PV/PVC
- `aws/`: EC2 `k3s` 실습

