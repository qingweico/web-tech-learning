# kafka/Dockerfile
FROM maven:3.9.5-eclipse-temurin-17 AS build
# WORKDIR 指定了容器内部的当前工作目录
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
# copy 用于将文件或目录从构建上下(即构建镜像时的本地目录)复制到镜像中的某个目录
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jre-ubi9-minimal
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8082/actuator/health || exit 1

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
