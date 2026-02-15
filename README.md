# 우리아기 API

### 관련 기술

- Spring Boot 2.6.4
- Java 24
- Gradle

### 패키지 구조(뼈대)

```
mybabycare.api
├── child
│   ├── controller
│   ├── dto
│   └── service
├── integration
│   ├── config
│   ├── email
│   ├── slack
│   └── vaccination
└── parent
    ├── controller
    ├── dto
    └── service
```

### API 경로

- `GET /v1/children`
- `POST /v1/children`
- `GET /v1/children/{childId}`
- `PATCH /v1/children/{childId}`
- `DELETE /v1/children/{childId}`

- `GET /v1/parents`
- `POST /v1/parents`
- `GET /v1/parents/{parentId}`
- `PATCH /v1/parents/{parentId}`
- `DELETE /v1/parents/{parentId}`

### 외부 연동 클라이언트

- `SlackClient`: Slack 메시지 전송 클라이언트
- `EmailClient`: 이메일 발송 API 연동 클라이언트
- `VaccinationApiClient`: 공공데이터포털(질병관리청 표준예방접종일정 조회서비스) 연동 클라이언트

### 공공 API 설정

`VaccinationApiClient`는 공공데이터포털 OpenAPI를 사용합니다.

- 기본 엔드포인트: `https://apis.data.go.kr/1471000/NIPTimetableService/getTimetableList`
- 필수 설정: `integration.vaccination.service-key`
- 응답 포맷: `_type=json`
