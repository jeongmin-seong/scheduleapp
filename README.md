# 일정 관리 애플리케이션 (Schedule Management API)

> Spring Boot 기반의 RESTful API 일정 관리 시스템

## 프로젝트 소개

일정을 효율적으로 관리하고 댓글을 통해 협업할 수 있는 백엔드 API 서비스입니다.
Session 기반 인증을 통해 보안을 강화하고, 페이징 기능으로 대용량 데이터를 효율적으로 처리합니다.

---

## 주요 기능

### ✅ 일정 CRUD
- 일정 생성, 조회, 수정, 삭제
- JPA Auditing을 활용한 자동 날짜 관리 (생성일, 수정일)
- 수정일 기준 내림차순 정렬

### ✅ 유저 CRUD 및 연관관계
- 유저 생성, 조회, 수정, 삭제
- 일정과 유저 간 다대일(N:1) 연관관계 매핑
- 유저 고유 식별자를 통한 데이터 관리

### ✅ 회원가입
- 이메일 중복 확인
- 비밀번호 필드 추가
- Bean Validation을 통한 입력값 검증

### ✅ 로그인
- Cookie/Session 기반 인증
- Interceptor를 통한 인증 체크
- 회원가입, 로그인 API는 인증 제외
- 로그인 실패 시 401 Unauthorized 반환


### ✅ 댓글 CRUD
- 일정에 댓글 작성, 조회, 수정, 삭제
- 댓글과 일정, 유저 간 연관관계 매핑
- N+1 문제 해결 (Fetch Join 적용)

### ✅  일정 페이징 조회
- Spring Data JPA Pageable 활용
- 댓글 개수 포함 조회
- 기본 페이지 크기: 10개
- 수정일 기준 내림차순 정렬

---

## 프로젝트 구조

```
src/main/java/com//scheduleapp/
├── common/                     # 공통 상수
│   └── SessionConst.java      # 세션 상수
├── config/                     # 설정 클래스
│   ├── JpaConfig.java         # JPA Auditing 설정
│   ├── PasswordEncoder.java   # 비밀번호 암호화
│   └── WebConfig.java         # Interceptor 설정
├── controller/                 # API 컨트롤러
│   ├── AuthController.java    # 인증 API
│   ├── ScheduleController.java # 일정 API
│   ├── UserController.java    # 유저 API
│   └── CommentController.java # 댓글 API
├── dto/                        # 데이터 전송 객체
│   ├── request/
│   │   ├── ScheduleCreateRequest.java
│   │   ├── ScheduleUpdateRequest.java
│   │   ├── UserCreateRequest.java
│   │   ├── UserUpdateRequest.java
│   │   ├── LoginRequest.java
│   │   ├── SignupRequest.java
│   │   ├── CommentCreateRequest.java
│   │   └── CommentUpdateRequest.java       
│   └── response/
│       ├── ErrorResponse.java
│       ├── ScheduleResponse.java
│       ├── UserResponse.java
│       ├── LoginResponse.java
│       ├── CommentResponse.java
│       └── SchedulePageResponse.java
├── entity/                     # 엔티티
│   ├── BaseEntity.java    # 날짜 추상 클래스
│   ├── Schedule.java          # 일정 엔티티
│   ├── User.java              # 유저 엔티티
│   └── Comment.java           # 댓글 엔티티
├── exception/                  # 예외 처리
│   ├── BusinessException.java # 비즈니스 예외
│   ├── ErrorCode.java         # 에러 코드
│   ├── GlobalExceptionHandler.java # 전역 예외 핸들러
│   ├── InvalidRequestException # 잘못된 요청 예외
│   ├── ResourceNotFoundException # 리소스를 찾을 수 없을 때 예외
│   └── UnauthorizedException # 인증 실패 예외
├── interceptor/                # 인터셉터
│   └── LoginCheckInterceptor.java # 로그인 체크
├── repository/                 # 레포지토리
│   ├── ScheduleRepository.java
│   ├── UserRepository.java
│   └── CommentRepository.java
├── service/                    # 서비스
│   ├── AuthService.java
│   ├── ScheduleService.java
│   ├── UserService.java
│   └── CommentService.java
└── ScheduleApplication.java   # 메인 클래스
```
---

## 🧩 ERD (Entity Relationship Diagram)
```
┌─────────────────────┐
│       users         │
├─────────────────────┤
│ id (PK)             │
│ username            │
│ email (UNIQUE)      │
│ password            │
│ created_at          │
│ updated_at          │
└─────────────────────┘
          │
          │ 1
          │
          │ N
┌─────────────────────┐         ┌─────────────────────┐
│     schedules       │         │      comments       │
├─────────────────────┤         ├─────────────────────┤
│ id (PK)             │ 1     N │ id (PK)             │
│ user_id (FK) ───────┼─────────┤ user_id (FK)        │
│ title               │         │ schedule_id (FK) ───┤
│ content             │         │ content             │
│ created_at          │         │ created_at          │
│ updated_at          │         │ updated_at          │
└─────────────────────┘         └─────────────────────┘
```

---

## 📘 API 명세서

### 🧑‍💼 User 관련 API

| 기능 | Method | URL | 요청 본문 | 응답 예시 | 비고 |
|------|--------|-----|------------|------------|------|
| 유저 생성 (회원가입) | POST | /api/users/signup | `{ "username": "홍길동", "email": "hong@test.com", "password": "1234" }` | `{ "id": 1, "username": "홍길동", "email": "hong@test.com" }` | 비밀번호 암호화 예정 |
| 유저 전체 조회 | GET | /api/users | - | `[ { "id": 1, "username": "홍길동", "email": "hong@test.com" } ]` | 관리자용 |
| 유저 단건 조회 | GET | /api/users/{id} | - | `{ "id": 1, "username": "홍길동", "email": "hong@test.com" }` | |
| 유저 수정 | PUT | /api/users/{id} | `{ "username": "홍길순", "email": "gilsun@test.com" }` | `{ "id": 1, "username": "홍길순", "email": "gilsun@test.com" }` | |
| 유저 삭제 | DELETE | /api/users/{id} | - | `{ "message": "삭제 완료" }` | |

---

### 🗓️ Schedule 관련 API
| 기능 | Method | URL | 요청 본문 | 응답 예시 | 비고 |
|------|--------|-----|------------|------------|------|
| 일정 생성 | POST | /api/schedules | `{ "userId": 1, "title": "회의 준비", "content": "내일 오전 10시" }` | `{ "id": 1, "userId": 1, "title": "회의 준비", "content": "내일 오전 10시", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" }` | 인증 필요, title 10글자 이내 |
| 일정 전체 조회 | GET | /api/schedules | - | `[ { "id": 1, "userId": 1, "title": "회의 준비", "content": "내일 오전 10시", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" } ]` | |
| 일정 페이징 조회 | GET | /api/schedules/page | `?page=0&size=10` | `{ "content": [{ "id": 1, "title": "회의 준비", "content": "내일 오전 10시", "commentCount": 3, "username": "홍길동", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-02T15:00:00" }], "totalElements": 50, "totalPages": 5, "size": 10, "number": 0 }` | 수정일 기준 내림차순 |
| 일정 단건 조회 | GET | /api/schedules/{id} | - | `{ "id": 1, "userId": 1, "title": "회의 준비", "content": "내일 오전 10시", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" }` | |
| 일정 수정 | PUT | /api/schedules/{id} | `{ "title": "회의 변경", "content": "오후 2시로 변경" }` | `{ "id": 1, "title": "회의 변경", "content": "오후 2시로 변경", "updatedAt": "2025-01-02T11:00:00" }` | 인증 필요 |
| 일정 삭제 | DELETE | /api/schedules/{id} | - | `{ "message": "삭제 완료" }` | 인증 필요 |

---

### 💬 Comment 관련 API

| 기능 | Method | URL               | 요청 본문 | 응답 예시 | 비고 |
|------|--------|-------------------|------------|------------|------|
| 댓글 생성 | POST | /api/comments     | `{ "userId": 1, "content": "좋은 계획이네요!" }` | `{ "id": 1, "scheduleId": 1, "userId": 1, "content": "좋은 계획이네요!", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" }` | 인증 필요 |
| 댓글 전체 조회 | GET | /api/comments | - | `[ { "id": 1, "scheduleId": 1, "userId": 1, "username": "홍길동", "content": "좋은 계획이네요!", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" } ]` | |
| 댓글 단건 조회 | GET | /api/comments/{id} | - | `{ "id": 1, "scheduleId": 1, "userId": 1, "content": "좋은 계획이네요!", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" }` | |
| 특정 일정의 댓글 목록 조회 |/api/comments?scheduleId | - | `[ { "id": 1, "scheduleId": 1, "userId": 1, "username": "홍길동", "content": "좋은 계획이네요!", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" } ]` | |
| 특정 유저의 댓글 목록 조회 |/api/comments/user/{userId} | - | `{ "id": 1, "scheduleId": 1, "userId": 1, "content": "좋은 계획이네요!", "createdAt": "2025-01-01T10:00:00", "updatedAt": "2025-01-01T10:00:00" }` | |
| 댓글 수정 | PUT | /api/comments/{id} | `{ "content": "수정된 댓글입니다" }` | `{ "id": 1, "content": "수정된 댓글입니다", "updatedAt": "2025-01-02T11:00:00" }` | 인증 필요 |
| 댓글 삭제 | DELETE | /api/comments/{id} | - | `{ "message": "삭제 완료" }` | 인증 필요 |

---

### 🔐 로그인 / 인증 관련 API

| 기능 | Method | URL | 요청 본문 | 응답 예시 | 비고 |
|------|--------|-----|------------|------------|------|
| 로그인 | POST | /api/users/login | `{ "email": "hong@test.com", "password": "1234" }` | `{ "message": "로그인 성공" }` | 세션 생성 |
| 로그아웃 | POST | /api/users/logout | - | `{ "message": "로그아웃 완료" }` | 세션 삭제 |

**예외 처리**  
- 로그인 실패 시 HTTP `401 Unauthorized`  
  → `{ "error": "이메일 또는 비밀번호가 올바르지 않습니다." }`

---

### 🚨 예외 처리

| 상황 | HTTP Status | 응답 예시 |
|------|-------------|------------|
| 로그인 실패 (이메일/비밀번호 불일치) | 401 | `{ "error": "이메일 또는 비밀번호가 올바르지 않습니다." }` |
| 인증 필요 (미로그인 상태) | 401 | `{ "error": "로그인이 필요합니다." }` |
| 존재하지 않는 리소스 | 404 | `{ "error": "해당 일정을 찾을 수 없습니다." }` |
| Validation 실패 (제목 10글자 초과) | 400 | `{ "error": "할일 제목은 10글자 이내로 작성해주세요." }` |
| Validation 실패 (유저명 4글자 초과) | 400 | `{ "error": "유저명은 4글자 이내로 작성해주세요." }` |
| 필수 필드 누락 | 400 | `{ "error": "필수 항목을 모두 입력해주세요." }` |

---

### 🕓 Auditing 필드 자동 관리

| 필드명 | 설명 | 설정 |
|--------|------|------|
| created_at | 데이터 생성 시 자동 저장 | @CreatedDate |
| updated_at | 데이터 수정 시 자동 업데이트 | @LastModifiedDate |

---

### ✅ 버전별 요약

| 단계 | 주요 추가 기능 |
|------|----------------|
| Lv1 | 일정 CRUD + JPA Auditing |
| Lv2 | 유저 CRUD + Schedule ↔ User 연관관계 |
| Lv3 | 회원가입 + 비밀번호 필드 추가 |
| Lv4 | 로그인 / 로그아웃 (Cookie/Session 기반 인증) |
| Lv5 | Validation을 활용한 다양한 예외처리 (제목 10자, 유저명 4자 등) |
| Lv6 | 비밀번호 BCrypt 암호화 (PasswordEncoder 구현) |
| Lv7 | 댓글 CRUD + Comment ↔ Schedule, User 연관관계 |
| Lv8 | 일정 페이징 조회 (Pageable, Page 활용, 수정일 기준 내림차순) |

---

## 실행

### 1. 데이터베이스 설정

```sql
-- MySQL 접속
mysql -u root -p

-- 데이터베이스 생성
CREATE DATABASE schedule_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 (선택사항)
CREATE USER 'schedule_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON schedule_db.* TO 'schedule_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. application.yml 설정

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/schedule_db?serverTimezone=Asia/Seoul
    username: root
    password: your_password  # 본인의 MySQL 비밀번호
```

### 3. 애플리케이션 실행

```bash
# 프로젝트 클론
git clone <repository-url>
cd schedule-management

# 빌드
./gradlew clean build

# 실행
./gradlew bootRun
```

### 4. 접속 확인

```
http://localhost:8080
```

---

## 사용 예시

### 1. 회원가입

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "홍길동",
    "email": "hong@example.com",
    "password": "1234"
  }'
```

### 2. 로그인

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "hong@example.com",
    "password": "1234"
  }' \
  -c cookies.txt
```

### 3. 일정 생성

```bash
curl -X POST http://localhost:8080/api/schedules \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "userId": 1,
    "title": "스프링 부트 공부",
    "content": "JPA 학습하기"
  }'
```

### 4. 일정 페이징 조회

```bash
curl -X GET "http://localhost:8080/api/schedules/page?page=0&size=10" \
  -b cookies.txt
```

### 5. 댓글 작성

```bash
curl -X POST http://localhost:8080/api/comments \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "userId": 1,
    "scheduleId": 1,
    "content": "화이팅!"
  }'
```

___

## 주요 구현 사항

### 1. JPA Auditing
- `@CreatedDate`, `@LastModifiedDate`를 활용한 자동 날짜 관리
- `BaseTimeEntity` 추상 클래스로 공통 필드 관리

### 2. 연관관계 매핑
- User ↔ Schedule: 다대일(N:1) 양방향 연관관계
- User ↔ Comment: 다대일(N:1) 양방향 연관관계
- Schedule ↔ Comment: 다대일(N:1) 양방향 연관관계
- 지연 로딩(LAZY) 적용으로 성능 최적화

### 3. N+1 문제 해결
- Fetch Join을 사용하여 연관 엔티티를 한 번의 쿼리로 조회
- `@Query("SELECT c FROM Comment c JOIN FETCH c.user")`

### 4. 페이징 처리
- Spring Data JPA의 `Pageable` 인터페이스 활용
- `PageRequest.of(page, size)` 사용
- 댓글 개수를 포함한 페이징 응답

### 5. Session 기반 인증
- Cookie/Session을 활용한 Stateful 인증
- `LoginCheckInterceptor`로 인증 체크
- 인증 실패 시 401 Unauthorized 반환

### 6. 예외 처리
- 커스텀 예외 클래스 (`BusinessException`)
- 전역 예외 핸들러 (`@RestControllerAdvice`)
- 일관된 에러 응답 구조 (`ErrorResponse`)

---

## 성능 최적화

### 1. 지연 로딩 (Lazy Loading)
- 연관 엔티티를 실제 사용 시점에 조회
- 불필요한 데이터 조회 방지

### 2. Fetch Join
- N+1 문제 해결
- 한 번의 쿼리로 연관 엔티티 조회

### 3. 페이징
- 대용량 데이터를 페이지 단위로 조회
- `LIMIT/OFFSET`을 활용한 효율적인 쿼리
