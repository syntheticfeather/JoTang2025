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
FROM openjdk:25-ea-17-jdk-slim

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
    sudo docker login
    ```
    输入你的用户名和密码。
3.  **重新标记你的镜像**，使其符合 Docker Hub 的命名规范（`你的用户名/镜像名:标签`）：
    ```bash
    sudo docker tag my-spring-app:1.0.0 your-dockerhub-username/my-spring-app:1.0.0
    ```
4.  **推送镜像到 Docker Hub**：
    ```bash
    sudo docker push your-dockerhub-username/my-spring-app:1.0.0
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
      iamfs/my-pro:1.0.0
    ```

    **重要参数解释：**
    *   `--restart=always`：确保容器在意外退出或服务器重启后自动启动，非常适合生产环境。
    *   `-e`：设置环境变量。`SPRING_PROFILES_ACTIVE=prod` 是Spring Boot读取的配置，它会激活 `application-prod.properties` 文件中的配置。
    *   `-v`：**卷挂载**。这是**极其重要**的一步。
        *   `/host/path/to/logs`：是服务器上的一个真实目录，你需要提前创建（`sudo mkdir -p /host/path/to/logs`）。
        *   `/app/logs`：是你Spring Boot应用在容器内写入日志的路径（你需要确保你的应用配置`logging.file.path=/app/logs`）。
        *   这样做的目的是**数据持久化**。即使容器被删除，日志文件仍然保留在服务器上。同样，你也可以挂载配置文件。

---

# 总结

Dockerfile 书写

指令构建

```bash
Docker Build -t my-spring-app:1.0.0 .
Docker tag my-spring-app:1.0.0 your-dockerhub-username/my-spring-app:1.0.0
Docker login
Docker push your-dockerhub-username/my-spring-app:1.0.0
DOcker pull your-dockerhub-username/my-spring-app:1.0.0
Docker run -d -p 80:8080 --name my-spring-app-prod -v /host/path/to/logs:/app/logs -e "SPRING_PROFILES_ACTIVE=prod" iamfs/my-pro:1.0.0
```

对缓存的镜像和容器清除

```bash
docker rmi 镜像名/id
docker rm 容器名/id
```


# docker指令表：

| 指令 | 说明 |
| --- | --- |
| `docker build` | 构建镜像 |
| `docker run` | 运行容器 |
| `docker images` | 列出镜像 |
| `docker ps` | 列出容器 |
| `docker logs` | 查看容器日志 |
| `docker stop` | 停止容器 |
| `docker rm` |删除容器|
| `docker push` | 推送镜像到镜像仓库 |
| `docker pull` | 从镜像仓库拉取镜像 |
| `docker login` | 登录镜像仓库 |




| 指令 | 说明 |例子|
| --- | --- | -|
| `-t` | 给镜像打标签 | `docker build -t my-spring-app:1.0.0 .` |
| `-p` | 端口映射 | `docker run -p 8080:8080 my-spring-app:1.0.0` |
| `-d` | 后台运行 | `docker run -d -p 8080:8080 --name spring-app-container my-spring-app:1.0.0` |
| `--name` | 给容器起名 | `docker run -d -p 8080:8080 --name spring-app-container my-spring-app:1.0.0` |
| `--restart` | 重启策略 | `docker run -d --restart=always -p 8080:8080 --name spring-app-container my-spring-app:1.0.0` |
| `--memory` | 限制内存 | `docker run -d --memory=512m -p 8080:8080 --name spring-app-container my-spring-app:1.0.0` |
| `--cpus` | 限制CPU使用率 | `docker run -d --cpus="1.0" -p 8080:8080 --name spring-app-container my-spring-app:1.0.0` |
| `-e` | 设置环境变量 | `docker run -d -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 --name spring-app-container my-spring-app:1.0.0` |
| `-v` | 挂载卷 | `docker run -d -v /host/path/to/logs:/app/logs -p 8080:8080 --name spring-app-container my-spring-app:1.0.0` |
| `-q` | 精简输出 | `docker images -q` |