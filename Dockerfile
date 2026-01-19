# 1. Build Stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
# 실행 권한 부여 및 빌드 (테스트 제외하여 배포 속도 향상, 필요시 -x test 제거)
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 파일 업로드 디렉토리 생성 (주의: Railway 기본 디스크는 재시작 시 초기화됨)
RUN mkdir -p uploads

# 실행 포트 설정 (Railway는 PORT 환경 변수를 주입함)
ENV PORT=8080
EXPOSE 8080

# 앱 실행 (Render 무료 플랜 512MB 메모리 제한 대응을 위해 힙 메모리 350MB로 제한)
ENTRYPOINT ["java", "-Xmx350m", "-jar", "app.jar"]
