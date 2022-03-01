# MyBabyCare
MyBabyCare 서비스 프로젝트입니다.

## Architecture
MyBabyCare 프로젝트의 아키텍쳐 입니다.

MyBabyCare 서비스는 다음 기능을 제공합니다.
* 부모, 자녀 데이터 저장 및 조회
* 자녀 데이터 기반 예방점종 정보 데이터 제공
* 자녀 데이터 기반 발달정보 데이터 제공
* 예방접종 기간 도달 시 Webhook & Email 전송 제공

## API Specification
MyBabyCare 는 Rest API를 제공합니다. 
개발중인 API 명세는 개발계 MyBabyCare API 서비스가 제공합니다. 명세 확인은 다음 URL에서 확인할 수 있습니다.
* http://localhost:8080/docs

## Development

### 개발 환경 준비
* Java 11 버전을 사용합니다.
* IDE는 개인이 익숙한 개발 도구(VS Code, IntelliJ)를 선택합니다.

### 빌드
```bash
gradle build
```

### 프로세스로 로컬 실행
환경 변수 설정
실행