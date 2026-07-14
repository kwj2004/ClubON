# 로그인 유지·아이디 찾기·비밀번호 재설정 API 명세

## 1. 공통 사항

- Base URL: `/api/auth`
- 요청 Content-Type: `application/json`
- 별도의 Access Token 없이 호출할 수 있는 공개 API입니다.
- `expiresIn`, `refreshExpiresIn`의 단위는 초(second)입니다.

### 공통 성공 응답

| 필드 | 타입 | Nullable | 설명 |
|---|---|---:|---|
| `status` | `number` (integer) | N | HTTP 상태 코드 |
| `message` | `string` | N | 처리 결과 메시지 |
| `data` | `object` | Y | API별 응답 데이터. 데이터가 없으면 필드가 생략될 수 있음 |

### 공통 오류 응답

| 필드 | 타입 | Nullable | 설명 |
|---|---|---:|---|
| `status` | `number` (integer) | N | HTTP 상태 코드 |
| `error` | `string` | N | 서버 에러 코드 |
| `message` | `string` | N | 오류 설명 |
| `errors` | `array<object>` | Y | 필드 유효성 검사 실패 목록 |
| `errors[].field` | `string` | N | 오류가 발생한 요청 필드 |
| `errors[].reason` | `string` | N | 해당 필드의 오류 사유 |

```json
{
  "status": 400,
  "error": "INVALID_INPUT_VALUE",
  "message": "Refresh Token은 필수입니다.",
  "errors": [
    {
      "field": "refreshToken",
      "reason": "Refresh Token은 필수입니다."
    }
  ]
}
```

---

## 2. 로그인 및 로그인 유지

### 2.1 로그인

`POST /api/auth/login`

이메일과 비밀번호를 검증하고 Access Token을 발급합니다. `rememberMe=true`이면 로그인 유지에 사용할 Refresh Token도 함께 발급합니다.

#### 요청값

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|---|---|---:|---|---|
| `email` | `string` | Y | 공백 불가, 이메일 형식 | 가입 이메일 |
| `password` | `string` | Y | 공백 불가 | 비밀번호 |
| `rememberMe` | `boolean` | N | 기본값 `false` | 로그인 유지 여부 |

```json
{
  "email": "student@g.eulji.ac.kr",
  "password": "password123",
  "rememberMe": true
}
```

#### 성공 응답: HTTP 200

| `data` 필드 | 타입 | Nullable | 설명 |
|---|---|---:|---|
| `userId` | `number` (long) | N | 회원 ID |
| `name` | `string` | N | 회원 이름 |
| `studentId` | `string` | N | 학번 |
| `department` | `string` | N | 학과 |
| `role` | `string` | N | `ROLE_STUDENT`, `ROLE_CLUB_ADMIN`, `ROLE_SCHOOL_ADMIN` 중 하나 |
| `tokenInfo` | `object` | N | 토큰 정보 |
| `tokenInfo.grantType` | `string` | N | 항상 `Bearer` |
| `tokenInfo.accessToken` | `string` | N | JWT Access Token |
| `tokenInfo.expiresIn` | `number` (long) | N | Access Token 남은 유효시간. 기본 3,600초 |
| `tokenInfo.refreshToken` | `string` | Y | `rememberMe=true`일 때 발급되는 Refresh Token |
| `tokenInfo.refreshExpiresIn` | `number` (long) | Y | Refresh Token 유효시간. 기본 2,592,000초(30일) |

```json
{
  "status": 200,
  "message": "로그인에 성공했습니다.",
  "data": {
    "userId": 1,
    "name": "홍길동",
    "studentId": "20260001",
    "department": "의료IT학과",
    "role": "ROLE_STUDENT",
    "tokenInfo": {
      "grantType": "Bearer",
      "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
      "expiresIn": 3600,
      "refreshToken": "U_J3...",
      "refreshExpiresIn": 2592000
    }
  }
}
```

`rememberMe=false`이면 `refreshToken`과 `refreshExpiresIn`은 `null`입니다.

#### 에러 코드

| HTTP | 에러 코드 | 발생 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | 이메일/비밀번호 누락 또는 이메일 형식 오류 |
| 401 | `LOGIN_FAILED` | 이메일이 존재하지 않거나 비밀번호 불일치 |

### 2.2 Access/Refresh Token 재발급

`POST /api/auth/tokens/refresh`

유효한 Refresh Token으로 새로운 Access Token과 Refresh Token을 발급합니다. 재발급 성공 즉시 요청에 사용한 Refresh Token은 폐기되므로 다시 사용할 수 없습니다.

#### 요청값

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|---|---|---:|---|---|
| `refreshToken` | `string` | Y | 공백 불가 | 로그인 또는 이전 재발급 응답으로 받은 Refresh Token |

```json
{
  "refreshToken": "U_J3..."
}
```

#### 성공 응답: HTTP 200

| `data` 필드 | 타입 | Nullable | 설명 |
|---|---|---:|---|
| `grantType` | `string` | N | 항상 `Bearer` |
| `accessToken` | `string` | N | 새 JWT Access Token |
| `expiresIn` | `number` (long) | N | 기본 3,600초 |
| `refreshToken` | `string` | N | 새 Refresh Token |
| `refreshExpiresIn` | `number` (long) | N | 기본 2,592,000초 |

```json
{
  "status": 200,
  "message": "토큰이 재발급되었습니다.",
  "data": {
    "grantType": "Bearer",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 3600,
    "refreshToken": "new_refresh_token...",
    "refreshExpiresIn": 2592000
  }
}
```

#### 에러 코드

| HTTP | 에러 코드 | 발생 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | Refresh Token 누락, 존재하지 않음, 만료됨 또는 이미 폐기됨 |

### 2.3 로그아웃

`POST /api/auth/logout`

전달된 Refresh Token을 폐기합니다. 클라이언트는 성공 후 저장한 Access/Refresh Token을 모두 삭제해야 합니다. Access Token은 서버에서 즉시 폐기되지 않으며 자체 만료 시점까지 형식상 유효합니다.

#### 요청값

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|---|---|---:|---|---|
| `refreshToken` | `string` | Y | 공백 불가 | 폐기할 Refresh Token |

```json
{
  "refreshToken": "U_J3..."
}
```

#### 성공 응답: HTTP 200

```json
{
  "status": 200,
  "message": "로그아웃되었습니다."
}
```

존재하지 않거나 이미 폐기된 토큰을 전달해도 멱등성 보장을 위해 HTTP 200을 반환합니다.

#### 에러 코드

| HTTP | 에러 코드 | 발생 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | Refresh Token 필드가 누락되었거나 공백임 |

---

## 3. 아이디 찾기

### 3.1 가입 이메일 찾기

`POST /api/auth/login-id/find`

이 서비스의 로그인 아이디는 이메일입니다. 이름과 학번이 모두 일치하는 회원의 마스킹된 이메일을 반환합니다.

#### 요청값

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|---|---|---:|---|---|
| `name` | `string` | Y | 공백 불가 | 회원 이름 |
| `studentId` | `string` | Y | 공백 불가 | 학번 |

```json
{
  "name": "홍길동",
  "studentId": "20260001"
}
```

#### 성공 응답: HTTP 200

| `data` 필드 | 타입 | Nullable | 설명 |
|---|---|---:|---|
| `maskedEmail` | `string` | N | 아이디 앞 2자리를 제외한 나머지를 `*`로 마스킹한 이메일 |

```json
{
  "status": 200,
  "message": "가입 아이디를 확인했습니다.",
  "data": {
    "maskedEmail": "st*****@g.eulji.ac.kr"
  }
}
```

#### 에러 코드

| HTTP | 에러 코드 | 발생 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | 이름 또는 학번 누락 |
| 400 | `INVALID_INPUT_VALUE` | 이름과 학번이 일치하는 회원이 없음 |

---

## 4. 비밀번호 재설정

비밀번호 재설정은 `인증번호 발송 → 인증번호 확인 → 새 비밀번호 설정` 순서로 호출합니다.

### 4.1 재설정 인증번호 발송

`POST /api/auth/password-resets/send`

가입된 이메일로 5분 동안 유효한 6자리 인증번호를 발송합니다. 계정 존재 여부 노출을 방지하기 위해 미가입 이메일도 동일한 성공 응답을 반환하지만 메일은 발송하지 않습니다.

#### 요청값

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|---|---|---:|---|---|
| `email` | `string` | Y | 공백 불가, 이메일 형식 | 가입 이메일 |

```json
{
  "email": "student@g.eulji.ac.kr"
}
```

#### 성공 응답: HTTP 200

```json
{
  "status": 200,
  "message": "비밀번호 재설정 인증번호가 발송되었습니다."
}
```

#### 에러 코드

| HTTP | 에러 코드 | 발생 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | 이메일 누락 또는 이메일 형식 오류 |
| 500 | 서버 내부 오류 | 메일 서버 연결 또는 발송 실패 |

### 4.2 재설정 인증번호 확인

`POST /api/auth/password-resets/verify`

가장 최근에 발급된 인증번호를 확인하고, 성공하면 비밀번호 변경에 한 번만 사용할 수 있는 Reset Token을 반환합니다. Reset Token의 기본 유효시간은 10분입니다.

#### 요청값

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|---|---|---:|---|---|
| `email` | `string` | Y | 공백 불가, 이메일 형식 | 인증번호를 받은 이메일 |
| `code` | `string` | Y | 숫자 6자리 | 이메일로 받은 인증번호 |

```json
{
  "email": "student@g.eulji.ac.kr",
  "code": "123456"
}
```

#### 성공 응답: HTTP 200

| `data` 필드 | 타입 | Nullable | 설명 |
|---|---|---:|---|
| `resetToken` | `string` | N | 비밀번호 변경용 1회성 토큰 |
| `expiresIn` | `number` (long) | N | Reset Token 유효시간. 기본 600초 |

```json
{
  "status": 200,
  "message": "인증번호가 확인되었습니다.",
  "data": {
    "resetToken": "mY_reset_token...",
    "expiresIn": 600
  }
}
```

#### 에러 코드

| HTTP | 에러 코드 | 발생 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | 이메일/인증번호 누락 또는 형식 오류 |
| 400 | `INVALID_INPUT_VALUE` | 해당 이메일의 인증번호 발송 내역이 없음 |
| 400 | `INVALID_INPUT_VALUE` | 인증번호 불일치 또는 5분 만료 |

### 4.3 새 비밀번호 설정

`POST /api/auth/password-resets/reset`

인증번호 확인 단계에서 받은 Reset Token으로 새 비밀번호를 설정합니다. 성공하면 Reset Token을 사용 처리하고 해당 회원에게 발급되어 있던 모든 Refresh Token을 폐기합니다.

#### 요청값

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|---|---|---:|---|---|
| `resetToken` | `string` | Y | 공백 불가 | 인증번호 확인 응답으로 받은 Reset Token |
| `newPassword` | `string` | Y | 8~100자, 공백 문자열 불가 | 새 비밀번호 |

```json
{
  "resetToken": "mY_reset_token...",
  "newPassword": "newPassword123"
}
```

#### 성공 응답: HTTP 200

```json
{
  "status": 200,
  "message": "비밀번호가 변경되었습니다."
}
```

#### 에러 코드

| HTTP | 에러 코드 | 발생 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | Reset Token 또는 새 비밀번호 누락 |
| 400 | `INVALID_INPUT_VALUE` | 새 비밀번호가 8자 미만 또는 100자 초과 |
| 400 | `INVALID_INPUT_VALUE` | Reset Token이 존재하지 않음 |
| 400 | `INVALID_INPUT_VALUE` | Reset Token이 10분 만료되었거나 이미 사용됨 |

---

## 5. 설정값

| 설정 키 | 기본값 | 설명 |
|---|---:|---|
| `jwt.expiration` | `3600000` ms | Access Token 유효시간(1시간) |
| `auth.refresh-token.expiration` | `2592000000` ms | Refresh Token 유효시간(30일) |
| `auth.password-reset.code-expiration` | `300000` ms | 비밀번호 재설정 인증번호 유효시간(5분) |
| `auth.password-reset.token-expiration` | `600000` ms | Reset Token 유효시간(10분) |
