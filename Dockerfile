# 选择构建用基础镜像。使用Java 17以匹配项目配置
FROM maven:3.9.9-eclipse-temurin-17 as build

# 指定构建过程中的工作目录
WORKDIR /app

# 将源代码和配置文件拷贝到工作目录
COPY src /app/src
COPY settings.xml pom.xml /app/

# 执行代码编译命令，使用生产环境profile
# 自定义settings.xml, 选用国内镜像源以提高下载速度
RUN mvn -s /app/settings.xml -f /app/pom.xml clean package -P prod

# 选择运行时基础镜像（使用更小的Eclipse Temurin JRE）
FROM eclipse-temurin:17-jre-alpine

# 设置时区为上海
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

# 创建非root用户运行应用（安全最佳实践）
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 指定运行时的工作目录
WORKDIR /app

# 将构建产物jar包拷贝到运行时目录中
COPY --from=build /app/target/*.jar app.jar

# 暴露端口（使用8080而不是80，因为Spring Boot默认使用8080）
EXPOSE 8080

# 执行启动命令，使用生产环境profile
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]
