# Docker部署部分

### 第一部分：核心概念与本地准备

在开始之前，我们先理解一下工作流程：

1.  **编写 Dockerfile**：一个文本文件，包含了构建 Docker 镜像所需的所有指令。
2.  **构建镜像 (Build Image)**：根据 `Dockerfile` 的指令，将你的应用代码、依赖和环境打包成一个独立的、可移植的镜像。
3.  **运行容器 (Run Container)**：将镜像实例化，在一个隔离的环境中运行起来，这就成为了一个容器。

#### 步骤 1：确保你的 Spring Boot 项目可打包

在项目根目录下，使用 Maven 或 Gradle 打包，生成 `.jar` 文件。这是构建 Docker 镜像的基础。

```bash
# Maven
mvn clean package -DskipTests

# Gradle
gradle clean build -x test
```
检查 `target/` 或 `build/libs/` 目录下是否生成了 `your-app-name-0.0.1-SNAPSHOT.jar` 文件。

#### 步骤 2：创建 Dockerfile

在项目的**根目录**下创建一个名为 `Dockerfile` 的文件（没有后缀）。这个文件是构建镜像的蓝图。

我们将创建一个**两阶段构建**的 `Dockerfile`，这是最佳实践。它可以生成更小、更安全的生产镜像。

```dockerfile
# 第一阶段：构建阶段 (Builder Stage)
# 使用一个包含Maven和JDK的官方镜像来构建项目
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
# 首先只复制pom.xml文件（利用Docker缓存层）
COPY pom.xml .
# 下载所有依赖（如果pom.xml没变，这一层会被缓存，极大加速后续构建）
RUN mvn dependency:go-offline
# 然后复制所有源代码
COPY src ./src
# 执行打包，生成jar文件
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段 (Runtime Stage)
# 使用一个更小的、仅包含JRE的官方镜像来运行应用，减小镜像体积
FROM openjdk:17-jre-slim

# 设置工作目录 inside the container
WORKDIR /app

# 从上一阶段（builder）复制打包好的jar文件
COPY --from=builder /app/target/*.jar app.jar

# 创建一个非root用户来运行应用，增强安全性
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# 暴露应用运行端口（与你application.properties中的server.port一致）
EXPOSE 8080

# 执行命令来运行应用
# 使用显式的"java -jar"命令而不是shell形式，确保能正确处理信号（如docker stop）
ENTRYPOINT ["java", "-jar", "/app.jar"]

# 可以添加JVM参数，例如调整内存、启用调试等（按需修改）
# ENTRYPOINT ["java", "-Xmx512m", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
```

**关键解释：**
*   `FROM`：指定基础镜像。`-slim` 或 `-alpine` 版本的镜像体积更小。
*   `WORKDIR`：设置容器内的工作目录。
*   `COPY`：从本地复制文件到镜像中。
*   `RUN`：在构建镜像时执行的命令。
*   `COPY --from=builder`：这是多阶段构建的关键，从上一个构建阶段复制文件，只把最终需要的 `.jar` 文件带过来，抛弃了构建环境的冗余文件。
*   `EXPOSE`：声明容器运行时监听的端口（这是一个元数据，实际映射需要在 `docker run` 时指定）。
*   `ENTRYPOINT`：指定容器启动时运行的命令。
*   **使用非 root 用户**：这是一个重要的安全实践。

#### 步骤 3：构建 Docker 镜像

在包含 `Dockerfile` 的项目根目录下，打开终端/命令行，执行以下命令：

```bash
# -t 参数给镜像打一个标签（名称:版本），方便后续使用
# 注意最后有一个点 (.)，表示当前目录是构建上下文
docker build -t my-spring-app:1.0.0 .

# 构建成功后，查看镜像列表
docker images
```
你应该能看到一个名为 `my-spring-app`，标签为 `1.0.0` 的镜像。

#### 步骤 4：在本地运行 Docker 容器测试

在部署到服务器之前，先在本地测试一下镜像是否能正常工作。

```bash
# 最基本的运行命令
docker run -p 8080:8080 my-spring-app:1.0.0

# 更实用的后台运行命令（添加 -d 参数）
docker run -d -p 8080:8080 --name spring-app-container my-spring-app:1.0.0
```

*   `-p 8080:8080`：将**宿主机的 8080 端口**映射到**容器的 8080 端口**。这样你访问 `http://localhost:8080` 就能访问到容器内的应用。
*   `-d`：让容器在**后台**运行（Detached mode）。
*   `--name`：给容器起一个名字，方便管理。

**测试：** 打开浏览器访问 `http://localhost:8080`。如果应用正常响应，说明镜像构建成功！

**查看日志：**
```bash
docker logs -f spring-app-container
```

**停止容器：**
```bash
docker stop spring-app-container
```

---

### 第二部分：部署到服务器

现在我们将本地构建好的镜像部署到远程服务器。

#### 步骤 1：将镜像上传到镜像仓库（Docker Hub）

你不能直接把本地镜像复制到服务器，最佳实践是使用镜像仓库（Registry）作为中介。Docker Hub 是免费的公共仓库。

1.  **注册 Docker Hub 账号**（如果你还没有）：https://hub.docker.com/
2.  **在本地登录 Docker Hub**：
    ```bash
    docker login
    ```
    输入你的用户名和密码。
3.  **重新标记你的镜像**，使其符合 Docker Hub 的命名规范（`你的用户名/镜像名:标签`）：
    ```bash
    docker tag my-spring-app:1.0.0 your-dockerhub-username/my-spring-app:1.0.0
    ```
4.  **推送镜像到 Docker Hub**：
    ```bash
    docker push your-dockerhub-username/my-spring-app:1.0.0
    ```
    推送成功后，你可以在 Docker Hub 网站的个人仓库里看到它。

#### 步骤 2：在服务器上拉取并运行镜像

通过 SSH 连接到你的远程服务器。

1.  **安装 Docker**（如果服务器上尚未安装）：
    官方安装脚本是最简单的方式（请始终从官方文档获取最新命令）：
    ```bash
    # 对于大多数Linux发行版
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER # 将当前用户加入docker组，避免每次用sudo
    # 执行完 usermod 后，需要退出SSH再重新登录生效
    ```

2.  **从 Docker Hub 拉取镜像**：
    ```bash
    docker pull your-dockerhub-username/my-spring-app:1.0.0
    ```

3.  **以生产环境方式运行容器**：
    我们使用更完善的命令，包含资源限制、重启策略和日志管理。

    ```bash
    docker run -d \
      --name my-spring-app-prod \
      -p 80:8080 \  # 将宿主机的80端口（HTTP默认端口）映射到容器的8080端口
      --restart=always \ # 容器总是重启，除非手动停止（实现开机自启）
      --memory=512m \ # 限制容器最大使用内存为512MB
      --cpus="1.0" \ # 限制容器最多使用1个CPU核心
      -e "SPRING_PROFILES_ACTIVE=prod" \ # 设置环境变量，指定Spring使用生产环境配置
      -v /host/path/to/logs:/app/logs \ # 挂载日志卷，将容器内日志持久化到主机磁盘
      your-dockerhub-username/my-spring-app:1.0.0
    ```

    **重要参数解释：**
    *   `--restart=always`：确保容器在意外退出或服务器重启后自动启动，非常适合生产环境。
    *   `-e`：设置环境变量。`SPRING_PROFILES_ACTIVE=prod` 是Spring Boot读取的配置，它会激活 `application-prod.properties` 文件中的配置。
    *   `-v`：**卷挂载**。这是**极其重要**的一步。
        *   `/host/path/to/logs`：是服务器上的一个真实目录，你需要提前创建（`sudo mkdir -p /host/path/to/logs`）。
        *   `/app/logs`：是你Spring Boot应用在容器内写入日志的路径（你需要确保你的应用配置`logging.file.path=/app/logs`）。
        *   这样做的目的是**数据持久化**。即使容器被删除，日志文件仍然保留在服务器上。同样，你也可以挂载配置文件。

---

### 第三部分：更高级的管理方式 - Docker Compose

对于生产环境，使用一个 `docker-compose.yml` 文件来管理所有容器配置比一长串 `docker run` 参数更清晰、更易维护。

1.  **在服务器上安装 Docker Compose**：
    ```bash
    # 参考官方最新文档：https://docs.docker.com/compose/install/
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    ```

2.  **在服务器上创建项目目录并编写 `docker-compose.yml`**：
    ```bash
    mkdir ~/my-app
    cd ~/my-app
    vim docker-compose.yml
    ```

3.  **编辑 `docker-compose.yml` 文件**：
    ```yaml
    version: '3.8'

    services:
      app:
        image: your-dockerhub-username/my-spring-app:1.0.0
        container_name: my-spring-app-compose
        restart: always
        ports:
          - "80:8080" # 主机端口:容器端口
        environment:
          - SPRING_PROFILES_ACTIVE=prod
        deploy: # 资源限制，仅在 compose spec 中有效
          resources:
            limits:
              memory: 512m
              cpus: '1.0'
        volumes:
          - ./logs:/app/logs # 使用相对路径，会在docker-compose.yml同目录下创建logs文件夹
        # networks: # 如果需要连接其他容器（如数据库），可以定义网络
        #   - app-network

    # volumes: # 如果需要命名卷，可以在这里声明
    #   app-logs:

    # networks: # 声明网络
    #   app-network:
    #     driver: bridge
    ```

4.  **使用 Docker Compose 启动服务**：
    ```bash
    # 在 docker-compose.yml 文件所在目录下执行
    docker-compose up -d # -d 表示后台运行
    ```

5.  **管理命令**：
    ```bash
    docker-compose logs -f app # 查看日志
    docker-compose stop        # 停止服务
    docker-compose start       # 启动服务
    docker-compose down        # 停止并删除容器
    docker-compose pull && docker-compose up -d # 更新镜像并重新部署
    ```

### 总结与后续步骤

你已经成功地将 Spring Boot 应用通过 Docker 部署到了服务器。

**下一步可以考虑：**

1.  **配置 Nginx 反向代理**：在 Docker 前面放置一个 Nginx，用于处理静态资源、负载均衡、SSL 加密（HTTPS）等。
2.  **设置 CI/CD 管道**：使用 Jenkins、GitLab CI/CD 或 GitHub Actions，实现代码推送后自动构建镜像、推送到仓库并在服务器上拉取更新。
3.  **容器编排**：如果应用规模变大，需要管理多个容器（如应用、数据库、缓存等），可以学习使用 `Docker Swarm` 或 `Kubernetes (K8s)`。
4.  **监控**：使用 `cAdvisor`, `Prometheus` 和 `Grafana` 来监控容器和应用的性能指标。

Docker 化部署是现代化应用部署的基石，祝你部署顺利！