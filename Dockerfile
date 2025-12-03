# 第一阶段：使用Maven构建
FROM maven:3.9.9-eclipse-temurin-17 as builder

WORKDIR /app
COPY . .

# 构建应用，跳过测试，清理Maven缓存
RUN mvn clean package -P prod -DskipTests -Dmaven.test.skip=true && \
    # 清理Maven缓存和构建文件
    rm -rf /root/.m2 && \
    rm -rf /app/target/*-sources.jar /app/target/*-javadoc.jar

# 第二阶段：使用极简Alpine镜像
FROM alpine:3.20 as runtime

# 安装最小化的OpenJDK JRE（只安装运行所需）
RUN apk add --no-cache \
    openjdk17-jre \
    tzdata && \
    # 设置时区
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    # 清理apk缓存
    rm -rf /var/cache/apk/*

# 创建非root用户
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# 只复制必要的jar文件
COPY --from=builder /app/target/*.jar app.jar

# 创建必要的目录
RUN mkdir -p /tmp/logs

EXPOSE 8080

# 优化后的JVM启动参数
ENTRYPOINT ["java", \
    "-Xmx512m", \
    "-Xms256m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=150", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dfile.encoding=UTF-8", \
    "-jar", "app.jar", \
    "--spring.profiles.active=prod"]
