# Boasse Backend Service

보아세(Boasse) 서비스의 백엔드 시스템입니다. 공지사항, 제품 소개, 협력사 관리, 채용 공고 및 고객 문의 기능을 제공합니다.

## 🛠 기술 스택 (Tech Stack)

- **Language**: Java 17
- **Framework**: Spring Boot 3.4.1
- **Database**: MySQL / TiDB
- **Security**: Custom JWT Authentication (Interceptor 기반)
- **Build Tool**: Gradle
- **Deployment**: Docker 지원

## 🚀 주요 기능 (Features)

1.  **공지사항(Notice)**: 게시글 작성, 수정, 삭제 및 다중 파일 첨부.
2.  **제품 관리(Product)**: 서비스 제품 정보 관리.
3.  **협력사 관리(Partner)**: 파트너사 정보 관리.
4.  **채용 관리(Recruit)**: 채용 공고 관리.
5.  **고객 문의(Inquiry)**: 사용자 문의 접수 및 관리자 확인.
6.  **인증 시스템**: JWT 기반 관리자 로그인 시스템.

## ⚙️ 실행 및 배포 (Deployment)

### 1. 환경 변수 설정 (Environment Variables)
배포 시 보안 및 설정을 위해 다음 환경 변수를 주입해야 합니다.

| 환경 변수명 | 설명 | 기본값 (권장 변경) |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | 데이터베이스 접속 URL | TiDB Cloud 주소 |
| `SPRING_DATASOURCE_USERNAME` | DB 사용자명 | - |
| `SPRING_DATASOURCE_PASSWORD` | DB 비밀번호 | - |
| `AUTH_ADMIN_PASSWORD` | 관리자 로그인 비밀번호 | `secure_password_123!` |
| `JWT_SECRET` | JWT 서명용 비밀키 | (매우 긴 무작위 문자열 권장) |
| `FILE_DIR` | 업로드 파일 저장 경로 | `uploads` |
| `CORS_ALLOWED_ORIGINS` | 허용할 프론트엔드 도메인 | `http://localhost:3000` 등 |

### 2. 파일 저장소 설정 (필수)
본 애플리케이션은 사용자가 업로드한 파일을 로컬 디렉토리에 저장합니다.
- **주의**: 컨테이너(Docker) 배포 시, `FILE_DIR`로 지정된 경로를 **호스트 볼륨(Volume Mount)**으로 연결해야 서버 재시작 시 파일이 유실되지 않습니다.
- **예시 (Docker)**: `-v /home/user/boasse_uploads:/app/uploads`

### 3. 빌드 및 실행
```bash
# JAR 파일 생성
./gradlew clean bootJar

# 실행
java -jar build/libs/boasse-backend-0.0.1-SNAPSHOT.jar
```

## 🔐 인증 안내
- **관리자 로그인**: `POST /api/v1/auth/login`
- **인증 방식**: Bearer Token (Authorization 헤더에 `Bearer {token}` 포함)
- 관리자 권한이 필요한 API(작성, 수정, 삭제 등) 호출 시 반드시 토큰이 필요합니다.

## 📂 디렉토리 구조
```
src/main/java/hello/boassebackend/
├── domain          # 도메인별 로직 (auth, notice, product, inquiry 등)
│   ├── controller  # API 엔드포인트
│   ├── service     # 비즈니스 로직
│   ├── entity      # JPA 엔티티
│   └── dto         # 데이터 전송 객체
├── global          # 전역 설정 (config, interceptor, util, exception)
└── uploads         # 로컬 파일 저장소 (배포 시 볼륨 연결 필요)
```
