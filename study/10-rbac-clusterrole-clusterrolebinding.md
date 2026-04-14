# 10. RBAC, ClusterRole, ClusterRoleBinding

## 범위

- RBAC 큰 그림
- `ServiceAccount`, `Role`, `ClusterRole`
- `RoleBinding`, `ClusterRoleBinding`
- `kubectl auth can-i`로 권한 확인

## 핵심 개념

- 쿠버네티스 권한 문제는 대부분 `누가(ServiceAccount)` + `무엇을(Role/ClusterRole)` + `어떻게 묶었는가(RoleBinding/ClusterRoleBinding)` 로 정리된다.
- `Role`은 네임스페이스 범위 권한이고, `ClusterRole`은 클러스터 범위 권한이다.
- `RoleBinding`은 권한을 네임스페이스 안에서 연결하고, `ClusterRoleBinding`은 권한을 클러스터 전체에 연결한다.
- 같은 `ClusterRole`이라도 `RoleBinding`으로 묶으면 네임스페이스 범위로 제한된다는 점이 가장 중요하다.

## 체크리스트

- [ ] RBAC 4개 리소스 관계를 말로 설명할 수 있다.
- [ ] `Role`과 `ClusterRole` 차이를 설명할 수 있다.
- [ ] `RoleBinding`과 `ClusterRoleBinding` 차이를 설명할 수 있다.
- [ ] `ServiceAccount`가 왜 필요한지 설명할 수 있다.
- [ ] `kubectl auth can-i`로 권한 확인 흐름을 직접 실행해 본다.

## 시각 흐름

```text
ServiceAccount
    |
    v
Role 또는 ClusterRole
    |
    v
RoleBinding 또는 ClusterRoleBinding
    |
    v
Kubernetes API 접근 권한 부여
```

## 가장 중요한 포인트

- `Role` = 특정 네임스페이스에서만 유효
- `ClusterRole` = 클러스터 범위에서 재사용 가능한 권한
- `RoleBinding` = 네임스페이스 범위로 권한 연결
- `ClusterRoleBinding` = 클러스터 범위로 권한 연결

이 네 줄이 오늘 제일 중요하다.

## 예시 흐름

### 1. 특정 네임스페이스에서만 파드 읽기

- `Role` + `RoleBinding`
- 예: `default` 네임스페이스 안의 `pods`만 조회

### 2. 클러스터 전체에서 파드 읽기

- `ClusterRole` + `ClusterRoleBinding`
- 예: 여러 네임스페이스의 `pods` 조회

### 3. ClusterRole 을 만들고 네임스페이스 제한만 걸기

- `ClusterRole` + `RoleBinding`
- 예: 공통 읽기 권한 정의는 재사용하되, 실제 권한은 특정 네임스페이스로 제한

## 명령어 메모

```bash
kubectl get sa -A
kubectl get role -A
kubectl get rolebinding -A
kubectl get clusterrole
kubectl get clusterrolebinding
kubectl describe clusterrole <clusterrole-name>
kubectl describe clusterrolebinding <clusterrolebinding-name>
kubectl auth can-i list pods --as=system:serviceaccount:default:<serviceaccount-name> -A
```

## 실습 메모

- 최소 권한 원칙으로 시작:
- `cluster-admin`을 남발하면 안 되는 이유:
- `RoleBinding`으로 충분한데 `ClusterRoleBinding`을 쓰면 생기는 위험:
- 오늘 헷갈렸던 부분:

