# 07. Spring Boot, RDS, ECR, k3s

## 범위

- Spring Boot 빌드
- Docker 이미지 생성
- ECR push
- RDS 연결
- EC2 k3s에서 배포 및 업데이트

## 핵심 개념

- 애플리케이션 빌드와 클러스터 배포는 분리해서 생각하는 편이 좋다.
- ECR은 컨테이너 이미지를 저장하는 레지스트리다.
- EC2가 ECR 이미지를 pull하려면 권한이 필요하다.
- 애플리케이션은 RDS 주소, 계정 정보, 포트 등의 외부 설정이 맞아야 정상 연결된다.

## 체크리스트

- [ ] Spring Boot jar 빌드
- [ ] Docker 이미지 생성
- [ ] ECR 리포지토리 생성
- [ ] 이미지 태그 및 push
- [ ] EC2에서 ECR pull 권한 부여
- [ ] k3s에서 Deployment/Service 배포
- [ ] 이미지 업데이트 후 재배포

## 명령어 메모

```bash
./gradlew bootJar
docker build -t <image-name> .
docker tag <local-image> <ecr-uri>
docker push <ecr-uri>
kubectl apply -f .
kubectl rollout restart deployment <deployment-name>
```

## 실습 메모

- ECR URI:
- RDS endpoint:
- k3s 배포 결과:
- 업데이트 배포 시 확인한 점:

