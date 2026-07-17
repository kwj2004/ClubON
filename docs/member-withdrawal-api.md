# 회원 탈퇴 API

## 회원 탈퇴

- Method: `DELETE`
- URL: `/api/users/me`
- 인증: `Authorization: Bearer {accessToken}` 필수
- Content-Type: `application/json`

### 요청 본문

| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| `password` | `string` | Y | 현재 로그인 계정의 비밀번호 |

```json
{
  "password": "CurrentPassword1!"
}
```

### 성공 응답 (`200 OK`)

```json
{
  "status": 200,
  "message": "회원 탈퇴가 완료되었습니다.",
  "data": null
}
```

탈퇴가 완료되면 회원 상태가 `WITHDRAWN`으로 변경되고 탈퇴 시각이 저장됩니다. 이메일과 학번은 고유한 익명 값으로, 이름과 학과는 탈퇴 회원 표시값으로 변경됩니다. Refresh Token, 비밀번호 재설정 정보, 기존 이메일 인증 정보 및 동아리 가입 관계가 삭제되므로 기존 이메일과 학번으로 새 계정을 만들 수 있습니다. 게시글, 후기, 지원서, 활동기록 등 서비스 이력은 기존 회원 ID를 참조한 채 유지됩니다.

### 오류 응답

| HTTP | error | 발생 조건 |
|---:|---|---|
| `400` | `INVALID_INPUT_VALUE` | `password`가 누락되거나 빈 문자열인 경우 |
| `400` | `INVALID_PASSWORD` | 현재 비밀번호가 일치하지 않는 경우 |
| `401` | `UNAUTHORIZED` | Access Token이 없거나 만료·변조된 경우 |
| `404` | `MEMBER_NOT_FOUND` | 인증된 회원을 찾을 수 없는 경우 |
| `409` | `LAST_CLUB_ADMIN` | 회원이 동아리의 유일한 운영진인 경우 |

오류 예시:

```json
{
  "status": 409,
  "error": "LAST_CLUB_ADMIN",
  "message": "단독 운영진으로 등록된 동아리가 있어 탈퇴할 수 없습니다. 운영진을 위임한 후 다시 시도해 주세요."
}
```

### 프론트엔드 처리

성공 응답을 받은 뒤에만 Access Token, Refresh Token, `localStorage`, `sessionStorage`의 로그인 정보를 제거하고 메인 화면으로 이동합니다. 실패 응답일 때는 로그인 정보를 삭제하지 않습니다.
