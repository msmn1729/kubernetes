# 02. Pod, Nginx, Spring Boot

## 범위

- Pod 기본 개념
- Nginx Pod 띄우기
- Spring Boot Pod 띄우기
- 이미지 풀 정책과 디버깅

## 핵심 개념

- Pod는 하나 이상의 컨테이너를 감싸는 가장 작은 배포 단위다.
- 컨테이너가 떠 있어도 외부 접속이 바로 되는 것은 아니다.
- 로컬 이미지를 쓸 때는 이미지 이름과 pull policy를 신경 써야 한다.
- `describe`, `logs`, `exec`는 Pod 디버깅의 기본이다.

## 체크리스트

- [ ] Nginx Pod 생성
- [ ] Spring Boot Pod 생성
- [ ] `kubectl get pods -o wide` 확인
- [ ] `kubectl logs`로 상태 확인
- [ ] `kubectl exec`로 컨테이너 내부 접근
- [ ] 이미지 pull 실패 케이스 정리

## 명령어 메모

```bash
kubectl apply -f <pod-file>
kubectl get pods
kubectl describe pod <pod-name>
kubectl logs <pod-name>
kubectl exec -it <pod-name> -- sh
```

## 실습 메모

- Nginx Pod 결과:
- Spring Boot Pod 결과:
- 접속이 안 됐던 이유:
- 다음에 다시 볼 오류:

