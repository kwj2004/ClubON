# 활동 기록 이미지 업로드 API

## Presigned URL 발급

`POST /api/clubs/{clubId}/record-images/presigned-url`

인증: `ROLE_CLUB_ADMIN` Access Token 및 해당 동아리 `ADMIN` 멤버십 필요

요청:

```json
{
  "fileName": "activity.jpg",
  "contentType": "image/jpeg",
  "fileSize": 245821
}
```

| 필드 | 타입 | 필수 | 제약 |
|---|---|---:|---|
| `fileName` | string | Y | 최대 255자 |
| `contentType` | string | Y | `image/jpeg`, `image/png`, `image/webp` |
| `fileSize` | number(long) | Y | 1 이상, 최대 10MB |

성공: HTTP 200

```json
{
  "status": 200,
  "message": "활동 기록 이미지 업로드 URL이 발급되었습니다.",
  "data": {
    "uploadUrl": "https://s3...",
    "objectKey": "club-records/1/uuid.jpg",
    "expiresIn": 300
  }
}
```

프론트는 `uploadUrl`에 같은 `Content-Type`으로 파일을 PUT합니다.

```javascript
await fetch(uploadUrl, {
  method: "PUT",
  headers: { "Content-Type": file.type },
  body: file
});
```

업로드 성공 후 `objectKey`를 활동 기록 작성 요청의 `imageUrls`에 넣습니다.

```json
{
  "title": "활동 제목",
  "content": "활동 내용",
  "imageUrls": ["club-records/1/uuid.jpg"]
}
```

목록 및 상세 조회 응답에서는 비공개 S3 객체를 볼 수 있는 15분 유효 다운로드 URL로 변환해 반환합니다.

활동기록 상세 조회 응답에는 수정 요청에서 재사용할 원본 객체 키인 `imageKeys`와 화면 표시용 Presigned GET URL인 `imageUrls`가 함께 포함됩니다. 수정 요청에는 `imageUrls`가 아니라 `imageKeys` 값을 전달해야 합니다.

```json
{
  "imageKeys": ["club-records/1/uuid.jpg"],
  "imageUrls": ["https://s3-presigned-download-url..."]
}
```

## 오류

| HTTP | 코드 | 조건 |
|---:|---|---|
| 400 | `INVALID_INPUT_VALUE` | 형식, 크기, 파일명 또는 객체 키 오류 |
| 401 | `UNAUTHORIZED` | 로그인/토큰 오류 |
| 403 | `ACCESS_DENIED` | 해당 동아리 운영자가 아님 |
| 404 | `CLUB_NOT_FOUND` | 동아리가 없음 |
| 503 | `STORAGE_OPERATION_FAILED` | S3 자격 증명 또는 서명 처리 실패 |
