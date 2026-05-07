# 📅 일정 투표 시스템 (Schedule Vote System)

팀이나 친구들의 일정을 효율적으로 조율하는 웹 애플리케이션입니다.
관리자가 여러 날짜 후보를 제시하면, 사용자들이 가능/불가능을 체크하고 자동으로 집계됩니다.

---

## 🚀 주요 기능

- ✅ **사용자 인증** - 회원가입, 로그인 (JWT 기반)
- 📋 **일정 생성** - 관리자가 일정과 후보 날짜 생성
- 🔗 **공유 링크** - 초대 코드를 통한 쉬운 참여
- 📊 **투표 시스템** - 사용자가 각 날짜별로 가능/불가능 체크
- 📈 **자동 집계** - 실시간 결과 및 추천 날짜 계산
- 📱 **반응형 디자인** - 모바일/태블릿/PC 모두 최적화

---

## 🛠️ 기술 스택

### 백엔드
- Java 17
- Spring Boot 3.1.5
- Spring Security + BCrypt
- JPA/Hibernate
- MSSQL

### 프론트엔드
- HTML5
- CSS3 (반응형 - Mobile First)
- JavaScript (Vanilla + jQuery)
- Bootstrap 스타일 (커스텀)

### DevOps
- Docker (멀티 스테이지 빌드)
- Render (배포 플랫폼)
- Git/GitHub

---

## 📦 프로젝트 구조

```
schedule-vote-project/
├── src/
│   ├── main/
│   │   ├── java/com/schedule/
│   │   │   ├── ScheduleVoteApplication.java      /* 메인 클래스 */
│   │   │   ├── config/                           /* 설정 클래스 */
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/                       /* REST API 컨트롤러 */
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ScheduleController.java
│   │   │   │   └── VoteController.java
│   │   │   ├── service/                          /* 비즈니스 로직 */
│   │   │   │   ├── UserService.java
│   │   │   │   ├── ScheduleService.java
│   │   │   │   └── VoteService.java
│   │   │   ├── repository/                       /* 데이터 접근 */
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ScheduleRepository.java
│   │   │   │   ├── ScheduleDateRepository.java
│   │   │   │   └── UserResponseRepository.java
│   │   │   ├── entity/                           /* JPA 엔티티 */
│   │   │   │   ├── UserEntity.java
│   │   │   │   ├── ScheduleEntity.java
│   │   │   │   ├── ScheduleDateEntity.java
│   │   │   │   └── UserResponseEntity.java
│   │   │   └── dto/                              /* DTO 클래스 */
│   │   │       ├── UserSignupRequest.java
│   │   │       ├── UserLoginRequest.java
│   │   │       ├── ScheduleCreateRequest.java
│   │   │       ├── ScheduleResponse.java
│   │   │       ├── UserResponseRequest.java
│   │   │       └── VoteResultResponse.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── static/
│   │           ├── index.html                    /* 메인 페이지 */
│   │           ├── vote.html                     /* 투표 페이지 */
│   │           ├── css/
│   │           │   └── style.css                 /* 스타일 (반응형) */
│   │           └── js/
│   │               ├── app.js                    /* 메인 앱 로직 */
│   │               └── vote.js                   /* 투표 로직 */
│   └── test/
│       └── java/com/schedule/                    /* 테스트 */
├── db/
│   └── schema.sql                                /* 데이터베이스 스크립트 */
├── pom.xml                                       /* Maven 설정 */
├── Dockerfile                                    /* Docker 이미지 */
├── .gitignore
├── render.yaml                                   /* Render 배포 설정 */
└── README.md                                     /* 이 파일 */
```

---

## 🔧 로컬 개발 환경 설정

### 사전 요구사항
- Java 17 이상
- Maven 3.8 이상
- MSSQL 2019 이상 (또는 Docker)
- Git
- IntelliJ IDEA 또는 VS Code

### 1단계: 프로젝트 클론
```bash
git clone https://github.com/yourusername/schedule-vote.git
cd schedule-vote-project
```

### 2단계: MSSQL 데이터베이스 설정 (Docker)
```bash
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=YourPassword123!" \
  -p 1433:1433 --name mssql-server -d mcr.microsoft.com/mssql/server:2019-latest
```

### 3단계: 데이터베이스 생성
```bash
/* MSSQL 클라이언트 접속 */
sqlcmd -S localhost -U sa -P YourPassword123!

/* 데이터베이스 생성 */
CREATE DATABASE schedule_vote;
GO

/* 스크립트 실행 */
:r db/schema.sql
GO
```

### 4단계: 애플리케이션 설정 (application-dev.properties)
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=schedule_vote;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPassword123!
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

### 5단계: 애플리케이션 실행
```bash
/* Maven 빌드 및 실행 */
mvn clean install
mvn spring-boot:run

또는

/* IntelliJ: 마우스 우클릭 → Run ScheduleVoteApplication */
```

### 6단계: 접속
```
http://localhost:8080
```

---

## 🌐 Render에 배포하기

### 사전 준비
1. [Render.com](https://render.com) 가입
2. [GitHub](https://github.com) 리포지토리 생성
3. Azure SQL 또는 기타 클라우드 MSSQL 준비

### 배포 단계

#### 1. GitHub에 코드 푸시
```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/yourusername/schedule-vote.git
git push -u origin main
```

#### 2. Render 대시보드에서 새 서비스 생성
1. Render 로그인
2. "New +" → "Web Service"
3. GitHub 계정 연결
4. `schedule-vote` 리포지토리 선택
5. 다음 설정:
   - **Name**: schedule-vote
   - **Environment**: Docker
   - **Region**: Singapore (또는 가까운 지역)
   - **Plan**: Free 또는 Starter

#### 3. 환경 변수 설정
Render 대시보드에서 **Environment** 탭:

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:sqlserver://your-server.database.windows.net:1433;databaseName=schedule_vote;encrypt=true;trustServerCertificate=true
DATABASE_USER=your_username@your_server
DATABASE_PASSWORD=your_password
JWT_SECRET_KEY=your-secret-key-make-this-long-and-random
JWT_EXPIRATION_MS=86400000
PORT=8080
```

#### 4. 배포
"Deploy" 버튼 클릭 → 자동 배포 시작

#### 5. 데이터베이스 스크립트 실행
첫 배포 후 Azure SQL Management Studio에서 `db/schema.sql` 실행

### 자동 배포 설정
GitHub의 `main` 브랜치에 푸시하면 자동으로 Render에 배포됩니다.

---

## 📚 API 문서

### 인증 API

#### 회원가입
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "user123",
  "email": "user@example.com",
  "password": "password123"
}

Response: { "success": true, "userId": 1 }
```

#### 로그인
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user123",
  "password": "password123"
}

Response: { "success": true, "userId": 1, "username": "user123" }
```

#### 사용자 정보 조회
```http
GET /api/auth/user/#{userId}

Response: { "success": true, "userId": 1, "username": "user123", "email": "user@example.com" }
```

### 일정 API

#### 일정 생성
```http
POST /api/schedule/create?adminId=#{userId}
Content-Type: application/json

{
  "title": "팀 합주",
  "dates": ["2026-05-15", "2026-05-16", "2026-05-17"]
}

Response: { "success": true, "data": { "scheduleId": 1, "shareCode": "ABC12345", ... } }
```

#### 공유 코드로 일정 조회
```http
GET /api/schedule/#{shareCode}

Response: { "success": true, "data": { ... } }
```

#### 일정별 상세 조회
```http
GET /api/schedule/#{scheduleId}
```

#### 관리자의 일정 목록
```http
GET /api/schedule/admin/#{userId}
```

### 투표 API

#### 투표 제출
```http
POST /api/vote/submit
Content-Type: application/json

{
  "dateId": 1,
  "userName": "김철수",
  "isAvailable": true
}

Response: { "success": true, "message": "투표가 저장되었습니다" }
```

#### 투표 결과 조회
```http
GET /api/vote/result/#{scheduleId}

Response: {
  "success": true,
  "data": {
    "scheduleId": 1,
    "title": "팀 합주",
    "dates": ["2026-05-15", ...],
    "results": {
      "2026-05-15": {
        "date": "2026-05-15",
        "availableCount": 5,
        "unavailableCount": 2,
        "availableUsers": ["김철수", "이영희", ...],
        "unavailableUsers": ["박민수", ...]
      }
    },
    "recommendedDate": "2026-05-15"
  }
}
```

---

## 🎨 화면 및 UI

### 관리자 화면 (index.html)
1. **로그인/회원가입** - 모달 폼
2. **일정 생성** - 제목, 날짜 추가 폼
3. **생성된 일정 목록** - 카드형 레이아웃
   - 공유 링크 복사 버튼
   - 결과 보기 버튼

### 사용자 투표 화면 (vote.html)
1. **일정 정보** - 제목, 설명
2. **이름 입력** - 텍스트 인풋
3. **투표 달력** - 테이블 형식
   - 각 날짜별 "가능", "불가능" 버튼
4. **투표 결과** - 자동 표시
   - 날짜별 통계
   - 참여자 목록

### 모바일 반응형
- **375px ~ 480px**: 버튼 스택형, 테이블 폰트 축소
- **480px ~ 768px**: 2열 그리드
- **768px 이상**: 원본 레이아웃

---

## 🔐 보안

### 적용된 보안 기능
1. **비밀번호 암호화** - BCryptPasswordEncoder
2. **CORS 설정** - 모든 출처 허용 (필요시 제한)
3. **입력 검증** - 모든 API 엔드포인트에서 검증
4. **SQL Injection 방지** - JPA Prepared Statement 사용
5. **HTTPS** - Render에서 자동 제공

### 보안 강화 권장사항
- [ ] JWT 토큰 기반 인증 추가
- [ ] Rate limiting 추가
- [ ] 로깅 및 모니터링
- [ ] 정기적인 보안 업데이트

---

## 📊 성능 최적화

### 프론트엔드
- jQuery CDN 사용 (캐싱)
- CSS 미디어 쿼리 (Mobile First)
- 번들 크기 최소화

### 백엔드
- JPA N+1 쿼리 최적화
- 인덱스 생성 (DB 스크립트에 포함)
- 캐싱 (필요시 추가)

### 배포
- Docker 멀티스테이지 빌드 (이미지 크기 최소화)
- Render Free Tier (초기 테스트용)

---

## 🐛 주요 기능 및 알려진 문제

### 구현된 기능
- ✅ 사용자 회원가입/로그인
- ✅ 일정 생성 및 공유
- ✅ 투표 제출 및 결과 조회
- ✅ 모바일 반응형 디자인
- ✅ Docker 배포

### 향후 개선사항
- [ ] 실시간 결과 업데이트 (WebSocket)
- [ ] 댓글 기능
- [ ] 이메일 초대
- [ ] 캘린더 통합 (Google Calendar 등)
- [ ] 다크 모드
- [ ] 다국어 지원 (i18n)

---

## 📝 코딩 컨벤션

### 주석 형식
```java
/* 기능 설명 */
```

### 변수 네이밍
- **쿼리**: 대문자 + 언더스코어 (SCHEDULE_ID, USER_NAME)
- **Java/JS**: 카멜케이스 (scheduleId, userName)

### SQL
- 쉼표: 컬럼 앞에 배치
- Alias: 영문 대문자 한 글자

---

## 👤 작성자

수정 (sjjeong0228@gmail.com)

---

## 📄 라이센스

MIT License

---

## 📞 지원 및 문의

- 이슈 보고: GitHub Issues
- 기능 제안: GitHub Discussions
- 이메일: sjjeong0228@gmail.com

---

**마지막 업데이트**: 2026년 5월 7일

Happy Scheduling! 🎉
