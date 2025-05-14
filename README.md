# 💡 Spring Security + JWT 실습 프로젝트

## 🎯 목표

이 프로젝트는 **Spring Security와 JWT(Json Web Token)**를 활용한 인증/인가 시스템을 직접 구현하고 이해하는 것을 목표로 합니다.  
실무에서 가장 많이 사용되는 인증 구조를 단계별로 따라가며 학습할 수 있습니다.

---

## 🧩 주요 기능

| 기능     | 설명                                                   |
| -------- | ------------------------------------------------------ |
| 회원가입 | 사용자 정보를 DB에 저장 (`/register`)                  |
| 로그인   | 인증 성공 시 JWT 발급 (`/login`)                       |
| 인증     | JWT를 이용한 사용자 인증 처리                          |
| 인가     | 사용자 권한(Role)에 따라 접근 제어 (`/admin`, `/user`) |
| H2 콘솔  | 내장 DB 웹 콘솔 제공 (`/h2-console`)                   |

---

## 📦 프로젝트 구조

```
com.example.jwt
├── auth
│   ├── JwtTokenProvider.java         // JWT 생성/검증
│   ├── JwtAuthenticationFilter.java  // 인증 필터
│   ├── SecurityConfig.java           // 시큐리티 설정
│   ├── CustomUserDetails.java        // 사용자 정보
│   └── CustomUserDetailsService.java // 사용자 로딩
├── controller
│   └── AuthController.java           // 로그인/회원가입 API
├── domain
│   └── User.java
├── dto
│   └── AuthRequest.java              // 로그인/회원가입 요청 DTO
├── repository
│   └── UserRepository.java
└── JwtDemoApplication.java
```

---

## ⚙️ 의존성 (pom.xml)

```xml
<!-- JWT -->
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.11.5</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-jackson</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
```

---

## 📄 application.yml

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

logging:
  level:
    org.springframework.security: DEBUG
```

---

## 🔐 테스트 예시

### 1. 회원가입

```
POST /register
{
  "username": "mingu",
  "password": "1234"
}
```

### 2. 로그인

```
POST /login
{
  "username": "mingu",
  "password": "1234"
}
```

→ 응답으로 JWT 토큰이 문자열 형태로 반환됨

### 3. 보호된 리소스 요청

```
GET /user
Header: Authorization: Bearer {발급받은 JWT}
```

---

## ✅ 참고 사항

- `/h2-console` 사용 시 `SecurityConfig`에서 CSRF, FrameOptions 설정 필요
- 테스트용으로 비밀번호는 평문 저장 (실무에서는 반드시 암호화 필요)
- 토큰은 기본적으로 1시간 유효, `JwtTokenProvider` 내부에서 설정
