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
- `VaccinationApiClient`: 공공기관 예방접종 기간 조회 API 클라이언트
