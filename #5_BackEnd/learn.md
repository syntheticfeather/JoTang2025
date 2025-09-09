# 准备工作

sudo apt install openjdk-17-jdk
sudo apt install maven

java

项目包是从 https://start.spring.io/ 直接建立。然后复制到linux上，不过也可以直接Linux指令创建:
`curl https://start.spring.io/starter.tgz -d dependencies=web -d type=maven-project -d groupId=com.example -d artifactId=demo -d name=demo | tar -xzvf -`不过依赖挺难设置的，记不住。

项目用maven管理

sql:

sudo mysql -u root -p

CREATE DATABASE JoTang2025 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'gcc'@'%' IDENTIFIED BY '918273';
GRANT ALL PRIVILEGES ON JoTang2025.* TO 'gcc'@'%';
FLUSH PRIVILEGES;

EXIT;

配置 MySQL 允许远程连接（为了让 Windows 的 Navicat 连接）

sudo vim /etc/mysql/mysql.conf.d/mysqld.cnf

bind-address            = 0.0.0.0

sudo systemctl restart mysql

sudo ufw allow 3306

# 学习了一下黑马程序员的Springboot+vue教程的后端部分

采用RESTFUL风格

## lombok

@Data // 核心注解：一键生成所有以下内容
@NoArgsConstructor // 生成无参构造器
@AllArgsConstructor // 生成全参构造器
@Builder 链式调用，生成建造者模式的Builder

先addproduct

post请求
请求体里面传参

还要product的参数要求

第一个报错，pulisher外键约束。

怎么第一个的id是4呢

对应着我三次外键请求失误的情况sql一次，网址2次，所以成功的就得到了4.