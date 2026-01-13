# Boasse Backend Service

한국교통대학교 공지사항 관리 시스템을 위한 백엔드 서비스입니다.
이 프로젝트는 **Spring Boot 3**와 **Java 17**을 기반으로 구축되었으며, 공지사항의 조회, 작성, 수정, 삭제 및 파일 첨부 기능을 제공합니다.

## 🛠 기술 스택 (Tech Stack)

- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: MySQL (설정 필요, 기본 H2/In-memory 가능)
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle
- **Utilities**: Lombok

## 🚀 주요 기능 (Features)

1.  **공지사항 조회**: 페이징(Pagination) 처리된 목록 조회 및 상세 조회.
2.  **공지사항 관리 (관리자)**:
    *   공지사항 작성, 수정, 삭제.
    *   관리자 권한은 **비밀번호 인증**(`admin1234`) 방식을 사용합니다.
3.  **파일 첨부**:
    *   게시글 작성 및 수정 시 다중 파일 업로드 지원.
    *   서버 로컬 디렉토리(`uploads/`)에 파일 저장.

## ⚙️ 실행 방법 (Getting Started)

### 사전 요구사항
*   JDK 17 이상 설치
*   (선택) MySQL 데이터베이스 실행 (또는 `application.properties` 수정)

### 빌드 및 실행
```bash
# 프로젝트 루트 디렉토리에서 실행

# Windows
./gradlew.bat bootRun

# Mac/Linux
./gradlew bootRun
```

서버가 정상적으로 실행되면 `http://localhost:8080`에서 접근 가능합니다.

## 📝 API 명세 및 테스트

### API 문서
자세한 API 명세는 프로젝트 루트의 `notice-api.yaml` (OpenAPI 3.0) 파일을 참고하세요.

### 테스트 가이드
Postman 등을 이용한 테스트 방법은 `api_test_guide.txt` 파일에 상세히 기술되어 있습니다.

### 관리자 인증
- 게시글 작성/수정/삭제 시 **관리자 비밀번호**가 필요합니다.
- **기본 비밀번호**: `admin1234`
- **사용법**:
    - **생성/수정**: Body(form-data)에 `password` 필드 포함.
    - **삭제**: URL 쿼리 파라미터로 전달 (예: `DELETE /api/v1/notices/1?password=admin1234`)

## 📂 디렉토리 구조
```
src/main/java/hello/boassebackend/
├── controller  # API 요청 처리 (NoticeController)
├── service     # 비즈니스 로직 (NoticeService, FileStore)
├── repository  # DB 접근 (JPA Repository)
├── domain      # 엔티티 (Notice, Attachment)
├── dto         # 데이터 전송 객체 (Request/Response DTO)
└── exception   # 전역 예외 처리
```

## ⚠️ 주의사항
*   **파일 저장 경로**: 업로드된 파일은 프로젝트 루트의 `uploads/` 폴더에 저장됩니다. 해당 폴더의 쓰기 권한이 필요합니다.
