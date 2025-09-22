# 部署到服务器上

## 准备工作

### 先去阿里云整一台服务器

    这部分呢，领了阿里云的学生福利，然后选了个服务器。

    没什么可以说的

### 服务器上配置。

    我选择的是mysql和项目部署到同一台服务器上。所以mysql和redis的连接配置都要换成localhost。

    然后服务器上就创建相应的sql库，然后注册一下redis。这两步参考 sql.md 和 redis.md ，操作步骤一样

### 拉dockerhub上的镜像，运行容器

    docker操作见 docker.md ，这里不再赘述

### 对安全组，端口的配置。


在服务器上输入docker指令(查看容器是否正在运行):

    docker ps 

    CONTAINER ID   IMAGE                COMMAND                  CREATED        STATUS         PORTS                                     NAMES
    a4fa96cc0360   iamfs/my-pro:1.0.0   "java -jar /app/app.…"   17 hours ago   Up 2 minutes   0.0.0.0:80->8080/tcp, [::]:80->8080/tcp   my-spring-app-prod

为了验证，可以在服务器上进行curl的访问测试

```bash
curl -X POST http://localhost/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "testpassword123"
  }'  
```
如果正常返回你在开发时返回的json格式数据，那么容器就在运行

![服务器测试结果](serverImgs/ServerCurlSuccess.png "服务器测试结果")

查看PORTS一列， 可以看到我的项目使用服务器(宿主机)的80端口映射容器的8080端口。

所以我们接下来就需要配置80端口的安全组，允许来自任何来源的80端口的访问。

如果不配置，那么外部的请求将无法访问到服务器上的容器:

![访问无法到达容器](serverImgs/postmanFail.png "访问无法到达容器")

**那么要配哪些东西呢?**

通过一系列的查询，发现要配置阿里云服务器的安全组，还要配置服务器的防火墙。

#### 1. 阿里云服务器的安全组配置

阿里云服务器的安全组配置，新建一个可以允许来自任何来源的80端口的访问。

![阿里云安全组](serverImgs/AliYunSecuritySet.png "阿里云安全组")

![新建80端口的入方向](serverImgs/AliyunServerSet.png "postman测试结果")

#### 2. 服务器的防火墙配置

```bash
sudo ufw allow 80/tcp
sudo ufw status
sudo ufw reload
```

![服务器防火墙配置](serverImgs/ServerUfwSet.png "服务器防火墙配置")

两层配置好了之后，才能进行外部机子的测试

![虚拟机测试结果](serverImgs/VirSuccess.png "虚拟机测试结果")
![postman测试结果](serverImgs/postmanSuccess.png "postman测试结果")


## 问题处理

    刚才的图片中可能已经发现了，JDBC连不上，因为我们的docker部署的时候，sql和redis的访问地址也要变，之前是硬编码为开发环境的ip地址，现在肯定会连不上

# **那么对于sql和redis的链接，也有两种办法**

## 1.首先是像开发环境一样的在服务器上安装sql和redis

```bash
# 通过apt安装
sudo apt install mysql-server redis-server
```

这种方式呢在AI嘴里是一种比较繁琐且安全性低的方式。

    因为我们要打开服务器对于3306和6479也就是sql和redis的特定端口。

    然后配置文件中硬编码为服务器的ip地址，用户和密码也相应改变。

```yaml
datasource:
    # JDBC 连接字符串
    # 格式: jdbc:mysql://<主机地址>:<端口>/<数据库名>?参数键值对
    url: jdbc:mysql://服务器地址:3306/JoTang2025?useSSL=false
# redis 同理
```

嗯既然学都学到这了，那我们为什么只学这个部署呢，肯定也得学习更加现代化的**docker-compose**部署方式

## 2. 第二种方式是使用docker-compose

    这种呢不是在服务器上安装sql和redis，而是从docker上拉取sql和redis的镜像进行容器部署，

    也就是说不仅部署了我们自己的spring项目，也一起部署了sql和redis。

最关键的是:

    其实我们是把项目直接传到了服务器上，

    并没有完成最开始的生产环境的镜像生成，上传镜像，然后服务器拉取镜像，运行容器。

    而是服务器本地获取源代码，通过docker-compose，生成项目镜像然后完成容器运行，并且在必要的时候从dockerhub上拉取其他需要的镜像(比如redis和mysql)

### **怎么做呢:**

这时候呢需要先写docker-compose.yml配置文件了。

```yml
# docker-compose-dev.yml
version: '3.7'
services:
  app:
    build: .
    container_name: my-app-dev
    ports:
      - "8080:8080"
    # 关键：将本地代码目录“绑定挂载”到容器内，覆盖掉构建时复制的文件。
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      # mysql 配置
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/JoTang2025?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      - SPRING_DATASOURCE_USERNAME=gcc
      - SPRING_DATASOURCE_PASSWORD=1234 
      # redis 配置  
      - SPRING_REDIS_HOST=redis-cache  
      - SPRING_REDIS_PASSWORD=1234  
      - SPRING_REDIS_PORT=6379
    depends_on:
      - mysql-db
      - redis-cache

  mysql-db:
    image: mysql:8.0
    container_name: mysql-db-dev
    environment:
      MYSQL_ROOT_PASSWORD: my_strong_password
      MYSQL_DATABASE: JoTang2025
      MYSQL_USER: gcc
      MYSQL_PASSWORD: 1234
      # MYSQL_HOST: '%'
    volumes:
      - ./db-init:/docker-entrypoint-initdb.d  # 挂载初始化脚本目录
      - mysql_data:/var/lib/mysql
    # 映射端口：格式 - "宿主机端口:容器内端口"
    ports:
      - "3306:3306" # 可选，如果宿主机需要直接连接数据库才开放
    # MySQL 8.x 的认证插件配置
    command: 
      - --default-authentication-plugin=mysql_native_password
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci
    restart: unless-stopped


  redis-cache:
    image: redis:6-alpine
    container_name: redis-cache-dev
    command: redis-server --requirepass 1234 --appendonly yes
    ports:
      - "6379:6379" # 暴露端口到本地
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
```
这里面app配置的是我们的项目。

之后是其他需要的镜像，比如sql和redis。

### 但是这里的sql镜像，拉下来是没有初始化我们需要的库的。怎么办呢？

那我们就要自己配置数据库的信息，写sql的语句(init-db/01-init-tables.sql)，

并且映射到容器自动运行的文件夹中，(也就是/docker-entrypoint-initdb.d)

### redis较为简单。不需要像关系类数据库写初始化脚本。

### 使用 -f 指定使用开发版本的 compose 文件
### 进行本地开发环境测试
docker-compose -f docker-compose.yml up -d

然后上传代码到服务器。远程连接就可


sql的3306被占用了
WARNING: Image for service app was built because it did not already exist. To rebuild this image you must use `docker-compose build` or `docker-compose up --build`.
Creating mysql-db-dev ... 
Creating mysql-db-dev    ... error
WARNING: Host is already in use by another container


ERROR: for mysql-db-dev  Cannot start service mysql-db: driver failed programming external connectivity on endpoint mysql-db-dev (b4b9b9126f1883a8923296fc887ad511faffb160ed1b401a05
Creating redis-cache-dev ... done


ERROR: for mysql-db  Cannot start service mysql-db: driver failed programming external connectivity on endpoint mysql-db-dev (b4b9b9126f1883a8923296fc887ad511faffb160ed1b401a0530141bffb3a12a): Error starting userland proxy: listen tcp4 0.0.0.0:3306: bind: address already in use
ERROR: Encountered errors while bringing up the project.

项目连不上
Attaching to my-app-dev
my-app-dev     | Error: Unable to access jarfile /app/app.jar

之后可以用本机进行测试

curl -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "testpassword123"
  }'  