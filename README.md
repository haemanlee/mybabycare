# MyBabyCare
MyBabyCare 서비스 프로젝트입니다.

## Architecture
MyBabyCare 프로젝트의 아키텍쳐 입니다.

MyBabyCare 서비스는 다음 기능을 제공합니다.
* 부모, 자녀 데이터 저장 및 조회
* 자녀 데이터 기반 예방점종 정보 데이터 제공
* 자녀 데이터 기반 발달정보 데이터 제공
* 예방점종 기간 도달 시 Webhook & Email 전송 제공

## API Specification
MyBabyCare 는 Rest API를 제공합니다. 
개발중인 API 명세는 개발계 MyBabyCare API 서비스가 제공합니다. 명세 확인은 다음 URL에서 확인할 수 있습니다.
* http://localhost:8080/docs


> <확인> 스웨거 표준은 OAS 2.0를 따릅니다. OAS 3.0과 문법이 다르기 때문에 수정시 주의해야 합니다.

> <주의> API 명세를 변경할 때에는 스펙에 문제가 없는 지 확인 후 업데이트해야 합니다. [스웨거 파서](https://apitools.dev/swagger-parser/online/)와 같은 스웨거 문법 오류를 검토하는 온라인 서비스를 이용할 수 있습니다.

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
`config/config.local.js` 파일을 생성해야 합니다. **해당 파일은 git repository에 반영되지 않도록 주의해야 합니다.**
```javascript
var config = require('./config.global');

config.env = 'local';
// R#1 개발 과정에서 삭제 예정
config.storagePath = 'seoul-dev-okc2-sams-nlb-log/test';

config.accessKeyId = '****TWE3****JURK****';
config.secretAccessKey = 'F+s3i****oWlz****7Rhr****fCI5****zNqLsKX';

config.cognitoRegion = 'ap-northeast-2';
config.cognitoUserPoolId = 'ap-northeast-2_nvfgJWvgs';
config.cognitoClientId = '3drsjd8fmvftcpq2l62gr13pi2';

config.dbUser = 'admin';
config.dbPassword = 'Pa$$w0rd';
config.dbHost = '127.0.0.1';

module.exports = config;
```
실행
```bash
npm run start
```