# Redis
## 什么是 Redis？

Redis（Remote Dictionary Server）是一个开源的、基于内存的键值存储系统，它可以用作数据库、缓存和消息中间件。它支持多种数据结构，包括字符串、哈希、列表、集合、有序集合等。

**Redis 的核心特性:**

    基于内存：数据存储在内存中，读写速度极快

    持久化：支持 RDB 和 AOF 两种持久化方式，可以将内存数据保存到磁盘

    丰富的数据结构：支持字符串、哈希、列表、集合、有序集合等

    高性能：单机可达 10万+ QPS（每秒查询率）

    原子操作：所有操作都是原子性的，支持事务

    发布/订阅：支持消息发布订阅模式

**Redis的应用实景:**

    在你的项目中，Redis 可以用于以下场景：

    缓存：缓存热点数据，减少数据库压力

    会话存储：存储用户会话信息

    短信验证码缓存：存储和验证短信验证码

    API 限流：限制接口访问频率

    排行榜：利用有序集合实现排行榜功能

    消息队列：使用列表实现简单的消息队列

## 安装Redis

```bash
sudo apt update        
sudo apt install redis-server -y  
```

### 配置Redis允许远程连接

默认情况下，Redis为安全起见只允许本地连接（127.0.0.1）。要允许远程连接，需修改其配置文件:

编辑Redis配置文件 redis.conf:

`sudo vim /etc/redis/redis.conf`

    将 bind 指令从 127.0.0.1 改为 0.0.0.0

    sudo systemctl restart redis 或者直接重启

防火墙设置端口开放:

`sudo ufw allow 6379/tcp`

## Redis 图形化客户端工具(方便测试):

    RedisInsight (官方推荐)：Redis 官方提供的可视化工具，支持连接和管理 Redis 数据库。    

    打开 RedisInsight，点击 "Add Redis Database"。

    在连接设置中：

    Host：填写你的 Linux 虚拟机的静态 IP 地址（如 192.168.6.128）。

    Port：默认是 6379。

    Password：如果没设置就不用填，设置了就填    

### 引入Redis

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- 连接池依赖 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

### 配置Redis

```yml
spring:
  redis:
    host: 192.168.6.128  # Redis服务器地址
    port: 6379       # Redis服务器端口
    password:        # Redis密码，如果没有设置密码则为空
    database: 0      # 数据库索引（0-15）
    timeout: 3000ms  # 连接超时时间
    lettuce:
      pool:
        max-active: 8      # 连接池最大连接数
        max-idle: 8        # 连接池最大空闲连接数
        min-idle: 0        # 连接池最小空闲连接数
        max-wait: -1ms     # 连接池最大阻塞等待时间（负值表示没有限制）
```

```bash
# 关闭redis
sudo systemctl stop redis

#关闭redis的自启动
sudo systemctl disable redis

# 查看redis的状态
sudo systemctl status redis

# 查看6379端口的监听
sudo netstat -ntlp | grep 6379

# 关闭监听
sudo netstat -ntlp | grep 6379 | awk '{print $7}' | cut -d/ -f1 | xargs kill -9
```

## QuickStart

使用redis自带的StringRedisTemplate来操作redis。

```java
@Autowired
// String 映射 String的操作模板
private StringRedisTemplate stringRedisTemplate;

@Test
void testStringSetAndGet() {
    // 设置值
    stringRedisTemplate.opsForValue().set("greeting", "Hello, Redis!");

    // 获取值并断言
    String value = stringRedisTemplate.opsForValue().get("greeting");
    assertEquals("Hello, Redis!", value);

    System.out.println("成功从Redis获取值: " + value);
}
```

```java
@Autowired
// 可设置value的类型，如User
private RedisTemplate<String, Object> redisTemplate;

@Test
void testRedisTemplate() {
    // 设置为Object可以存任意类型，只不过最后要类型转换，也有可能导致类型不匹配
    redisTemplate.opsForValue().set("name", "John");
    String name = (String) redisTemplate.opsForValue().get("name");
    assertEquals("John", name);

    System.out.println("成功从Redis获取值: " + name);

    User user = new User(1L, "Tom", "123456", "USER", "13812345678", null, null);
    redisTemplate.opsForValue().set("user", user);
    User user2 = (User) redisTemplate.opsForValue().get("user");
    assertEquals(user, user2);

    System.out.println("成功从Redis获取值: " + user2);
}
```
    一般来说opsForValue就够用了，

    还有些其他的操作，比如opsForList、opsForSet、opsForZSet等。

    用到再说

## 为Product做缓存 (我们需要重写Redis的序列化器，以便更好地控制序列化和反序列化过程。)

    现在我想给product做一个缓存机制，让缓存热门商品，减轻数据库压力。

    但是:
        我们product类使用了LocalDateTime，LocalDateTime是Java8的新类型，默认的序列化器无法序列化LocalDateTime。

        还有就是二进制的存储格式不如json格式方便前期学习

    所以我们重写redis的value序列化器，让它可以序列化LocalDateTime。

```java
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    // 我们自己的RedisTemplate
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    // 连上工厂
    template.setConnectionFactory(factory);

    // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
    Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

    // 配置ObjectMapper
    ObjectMapper mapper = new ObjectMapper();

    // 注册Java 8日期时间模块
    mapper.registerModule(new JavaTimeModule());

    // 禁用日期序列化为时间戳的功能
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // 设置所有字段（包括私有字段）都可见
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

    // 启用默认类型信息
    mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
    );

    serializer.setObjectMapper(mapper);

    // 使用StringRedisSerializer来序列化和反序列化redis的key值
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);

    template.afterPropertiesSet();
    return template;
}
```

### 然后我查了一下Jackson2JsonRedisSerializer与默认序列化的对比，总结如下:
|特性	|默认配置 (JdkSerializationRedisSerializer)	|当前自定义配置 (Jackson2JsonRedisSerializer)|
|-|-|-|
|存储形式|	二进制 (不可读)|	JSON 字符串 (可读性高)|
|Java8 时间支持|	不支持 LocalDateTime 等类型|	支持 (通过 JavaTimeModule)|
|跨语言兼容性|	仅 Java 可解析	|所有语言可读 (Node/Python 等都能解析)|
|序列化性能	|较高 (二进制压缩)|	中等 (JSON 转换开销)|
|反序列化安全性|	有 RCE 风险	可控 |(通过 PolymorphicTypeValidator)|
|字段可见性|	只能序列化公共字段	|支持序列化所有字段 (包括私有字段)|

## 除了product我们还可以干嘛呢

#### 注: 以下功能并不能真正运行，阿里云/腾讯的短信推送凭证申请都没通过。以下只是思路和代码部分的实现。

### 1.做一个手机短信验证绑定功能

**步骤:**

    生成验证码（数字或字母数字组合）

    将验证码(value)与用户标识（手机号/邮箱）(前缀+手机号/邮箱)绑定并存入 Redis（设置有效期）

    通过第三方服务（短信网关/邮件服务器）发送验证码

    用户提交验证码，服务端从 Redis 中校验是否正确

    验证成功后执行后续业务（如注册、重置密码等）

**具体实现:**

新增AuthCodeService接口，定义发送验证码和验证验证码的接口

```java
// 生成并存储手机号验证码
@Override
public void savePhoneAuthCode(String phone) {
    String key = SMS_KEY_PREFIX + phone;
    String code = generateRandomCode();
    // 真正的发送短信逻辑
    // 应该是直接调用腾讯云的接口就可以了
    // smsSendCode(phone, code);
    
    redisUtil.set(key, code, SMS_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
}

// 将得到的验证码与存储的验证码做对比
@Override
public void validateCode(String phone, String code) {
    String key = SMS_KEY_PREFIX + phone;
    String redisCode = (String) redisUtil.get(key);
    if (redisCode == null) {
        throw new BusinessException(400, "验证码已过期");
    }
    if (!redisCode.equals(code)) {
        throw new BusinessException(400, "验证码错误");
    } else {
        redisUtil.delete(key);
    }
}
```

**然后真正实现短信的发送(采用腾讯云服务)(不过我一直没有通过腾讯的资质验证，目前还用不起)**

```xml
<dependency>
    <groupId>com.tencentcloudapi</groupId>
    <artifactId>tencentcloud-sdk-java-sms</artifactId>
    <version>3.1.1281</version>
</dependency>
```

**加腾讯云配置**
```yml
tencent:
  cloud:
    sms:
      secret-id: no  # 你的 SecretId
      secret-key: no # 你的 SecretKey
      sdk-app-id: no # 你的短信应用 ID（如 1400001234）
      sign-name: no     # 审核通过的签名内容
      template-id: no      # 审核通过的模板 ID
```


**添加短信发送工具类(smsUtil):**

这里直接抄的腾讯的QuickStart代码。

**然后再AuthCodeServiceImpl中引用接口:**

```java
if (smsUtil.sendSmsCode(phone, code) == false){
    throw new BusinessException(400, "验证码发送失败");
}
```
**伪短信发送的功能实现结束**

---

### 也可以做Order的缓存功能，但是和Product的编写差不多，就先不重复的写一遍了。

### 还有邮箱验证码的功能，可以参考上面的步骤实现。

























