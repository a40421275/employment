# 构建阶段
FROM maven:3.9.9-eclipse-temurin-17 as builder

WORKDIR /app

# 只复制必要的构建文件
COPY pom.xml settings.xml ./
COPY src ./src

# 下载依赖并构建（利用Docker缓存层）
RUN mvn dependency:go-offline -P prod -B

# 构建应用
RUN mvn clean package -P prod -DskipTests -Dmaven.test.skip=true -q

# 运行阶段 - 使用最小的JRE镜像
FROM eclipse-temurin:17-jre-alpine

# 设置时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata && \
    # 清理apk缓存
    rm -rf /var/cache/apk/*

# 创建非root用户
RUN addgroup -S app && adduser -S app -G app
USER app

WORKDIR /app

# 只复制构建的jar文件
COPY --from=builder --chown=app:app /app/target/*.jar app.jar

# 优化：移除JAR文件中的非必要内容（可选，如果需要进一步减小）
# RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

EXPOSE 8080

# 优化的JVM参数
ENTRYPOINT ["java", \
    "-Xmx512m", \
    "-Xms256m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dfile.encoding=UTF-8", \
    "-jar", "app.jar", \
    "--spring.profiles.active=prod"]
