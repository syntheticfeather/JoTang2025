# sql配置

## 在 Linux 上设置 MySQL 数据库

第一部分：在 Linux 虚拟机中创建数据库
步骤 1: 安装 MySQL Server
打开终端，根据你的 Linux 发行版执行命令：

Ubuntu

```bash
sudo apt update
sudo apt install mysql-server
```

# 安装后启动服务
```bash
sudo systemctl start mysql
```

## 步骤 2: 保护安装并设置 Root 密码

运行 MySQL 的安全脚本，它会引导你设置 root 密码并做一些安全设置（如移除匿名用户、禁止远程 root 登录等）。

```bash
sudo mysql_secure_installation
```

根据提示依次操作即可。

## 步骤 3: 登录 MySQL 并创建专属数据库和用户
强烈不建议在项目中直接使用 root 用户。我们应该为每个项目创建独立的数据库和用户。

### 以 root 身份登录 MySQL:

```bash
sudo mysql -u root -p
```
输入你刚刚设置的 root 密码。

### 创建专属数据库 (例如，命名为 my_project_db):

```sql
CREATE DATABASE my_project_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
utf8mb4 字符集支持存储所有 Unicode 字符（包括表情符号👍）。

创建专属用户 (例如，用户名为 my_project_user, 密码为 a_strong_password) 并授予其对新数据库的所有权限：

```sql
CREATE USER 'my_project_user'@'%' IDENTIFIED BY 'a_strong_password';
GRANT ALL PRIVILEGES ON my_project_db.* TO 'my_project_user'@'%';
FLUSH PRIVILEGES;
```
'my_project_user'@'%' 中的 '%' 表示允许该用户从任何主机连接。对于本地开发环境，这样设置没问题。生产环境应限制为具体IP。

退出 MySQL:

```sql
EXIT;
```

## 步骤 4: 配置 MySQL 允许远程连接（为了让 Windows 的 Navicat 连接）
默认情况下，MySQL 只监听本地连接 127.0.0.1。我们需要修改其配置。

编辑 MySQL 配置文件，通常位于 /etc/mysql/mysql.conf.d/mysqld.cnf(Ubuntu)

```bash
# Ubuntu
sudo vim /etc/mysql/mysql.conf.d/mysqld.cnf

bind-address            = 0.0.0.0
这表示允许 MySQL 接受来自任何网络接口的连接。

重启 MySQL 服务使配置生效：

# Ubuntu
sudo systemctl restart mysql

# Ubuntu (使用 ufw)
sudo ufw allow 3306

# 开机自启动
sudo systemctl enable mysql
# 关闭自启动
sudo systemctl disable mysql
```

现在，你应该可以在 Windows 主机上使用 Navicat，用 虚拟机IP、用户名 my_project_user 和密码 a_strong_password 成功连接到虚拟机中的 MySQL 了。